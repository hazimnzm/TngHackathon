package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Transaction;
import Hackathing.BackendTemplate.DTO.TransactionDTO;
import Hackathing.BackendTemplate.DTO.TransactionGroupDTO;
import Hackathing.BackendTemplate.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction addTransaction(
            long inventoryId,
            String person,
            String itemName,
            long itemCount,
            String category,
            String type,
            double amount
    ) {
        Transaction transaction = new Transaction();
        transaction.setInventoryId(inventoryId);
        transaction.setPerson(person);
        transaction.setItemName(itemName);
        transaction.setItemCount(itemCount);
        transaction.setCategory(category);
        transaction.setType(type);
        transaction.setAmount(amount);
        return transactionRepository.save(transaction);
    }

    public List<TransactionDTO> getTransaction(long inventoryId, String timeFilter, int displacement, String typeFilter) {
        TimeWindow window = resolveTimeWindow(timeFilter, displacement);
        List<Transaction> transactions = fetchTransactions(inventoryId, window.start(), window.end(), typeFilter);
        return transactions.stream().map(TransactionDTO::DOToDTO).collect(Collectors.toList());
    }

    public List<TransactionGroupDTO> getProfitLoss(long inventoryId, String timeFilter, String typeFilter) {
        // 1. Defaults
        if (timeFilter == null || timeFilter.trim().isEmpty()) timeFilter = "month";
        String normalizedTimeFilter = timeFilter.trim().toLowerCase();

        // Check if we should ignore types
        boolean shouldGroupByType = typeFilter != null && !typeFilter.isBlank();

        // 2. Fetch Data
        List<Transaction> allTransactions = transactionRepository.findByInventoryIdOrderByCreatedAtDesc(inventoryId);

        // 3. Dynamic Grouping Logic
        // The key here will be either "04/2026" OR "04/2026|Food"
        Map<String, List<Transaction>> grouped = new LinkedHashMap<>();

        for (Transaction t : allTransactions) {
            String periodKey = getPeriodKey(t, normalizedTimeFilter);
            String finalKey = periodKey;

            if (shouldGroupByType) {
                String typeValue = "category".equalsIgnoreCase(typeFilter) ? t.getCategory() : t.getItemName();
                if (typeValue == null || typeValue.isEmpty()) typeValue = "Uncategorized";
                // Combine period and type into one unique grouping key
                finalKey = periodKey + "|" + typeValue;
            }

            grouped.computeIfAbsent(finalKey, k -> new ArrayList<>()).add(t);
        }

        // 4. Transform to DTO
        List<TransactionGroupDTO> result = new ArrayList<>();

        grouped.forEach((compositeKey, transList) -> {
            String periodLabel;
            String typeLabel;

            if (shouldGroupByType) {
                // Split back the composite key
                String[] parts = compositeKey.split("\\|");
                periodLabel = getPeriodLabel(parts[0], normalizedTimeFilter);
                typeLabel = parts[1];
            } else {
                periodLabel = getPeriodLabel(compositeKey, normalizedTimeFilter);
                typeLabel = "All Transactions";
            }

            double total = transList.stream().mapToDouble(Transaction::getAmount).sum();
            List<TransactionDTO> dtos = transList.stream()
                    .map(TransactionDTO::DOToDTO)
                    .collect(Collectors.toList());

            result.add(new TransactionGroupDTO(periodLabel, typeLabel, total, dtos));
        });

        // Sort result: Newest time first, then by type label
        result.sort(Comparator.comparing(TransactionGroupDTO::getTimeRange).reversed()
                .thenComparing(TransactionGroupDTO::getType));

        return result;
    }

    private List<Transaction> fetchTransactions(long inventoryId, LocalDateTime start, LocalDateTime end, String typeFilter) {
        if (typeFilter == null || typeFilter.isBlank()) {
            return transactionRepository
                    .findByInventoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(inventoryId, start, end);
        }
        return transactionRepository
                .findByInventoryIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(
                        inventoryId, typeFilter, start, end
                );
    }

    private TimeWindow resolveTimeWindow(String timeFilter, int displacement) {
        if (displacement < 0) {
            throw new IllegalArgumentException("displacement must be 0 or greater");
        }

        String normalized = timeFilter == null ? "day" : timeFilter.trim().toLowerCase();
        LocalDateTime start;
        LocalDateTime end;

        switch (normalized) {
            case "day" -> {
                LocalDate date = LocalDate.now().minusDays(displacement);
                start = date.atStartOfDay();
                end = start.plusDays(1);
            }
            case "week" -> {
                LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(displacement);
                start = weekStart.atStartOfDay();
                end = start.plusWeeks(1);
            }
            case "month" -> {
                LocalDate monthStart = LocalDate.now().withDayOfMonth(1).minusMonths(displacement);
                start = monthStart.atStartOfDay();
                end = start.plusMonths(1);
            }
            default -> throw new IllegalArgumentException("timeFilter must be one of: day, week, month");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String label = start.toLocalDate().format(formatter) + " - " + end.minusDays(1).toLocalDate().format(formatter);
        return new TimeWindow(start, end, label);
    }

    // New helper: Get period key for grouping (e.g., "2026-4" for month)
    private String getPeriodKey(Transaction t, String timeFilter) {
        LocalDateTime dt = t.getCreatedAt();
        if (dt == null) return "unknown";  // Fallback for null dates
        LocalDate date = dt.toLocalDate();

        return switch (timeFilter) {
            case "day" -> date.toString();  // e.g., "2026-04-26"
            case "week" -> date.with(DayOfWeek.MONDAY).toString();  // e.g., "2026-04-21" (Monday of the week)
            case "month" -> date.getYear() + "-" + date.getMonthValue();  // e.g., "2026-4"
            default -> throw new IllegalArgumentException("Invalid timeFilter");
        };
    }

    // New helper: Get human-readable period label (e.g., "04/2026" for month)
    private String getPeriodLabel(String key, String timeFilter) {
        return switch (timeFilter) {
            case "day" -> {
                LocalDate date = LocalDate.parse(key);
                yield date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            case "week" -> {
                LocalDate start = LocalDate.parse(key);
                LocalDate end = start.plusDays(6);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                yield start.format(formatter) + " - " + end.format(formatter);
            }
            case "month" -> {
                String[] parts = key.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                yield String.format("%02d/%d", month, year);
            }
            default -> throw new IllegalArgumentException("Invalid timeFilter");
        };
    }

    public Double getTotalNetProfitLoss(long inventoryId) {
        // This will return a single double value (e.g., 3850.50 or -120.00)
        return transactionRepository.sumAmountByInventoryId(inventoryId);
    }

    private String normalizeType(String type) {
        return type == null || type.isBlank() ? "unknown" : type;
    }

    private record TimeWindow(LocalDateTime start, LocalDateTime end, String label) {
    }
}
