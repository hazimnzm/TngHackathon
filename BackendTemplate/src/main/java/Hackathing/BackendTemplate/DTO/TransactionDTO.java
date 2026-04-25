package Hackathing.BackendTemplate.DTO;

import Hackathing.BackendTemplate.DO.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private long inventoryId;
    private String person;
    private String itemName;
    private long itemCount;
    private String category;
    private String type;
    private double amount;
    private String date;

    public static TransactionDTO DOToDTO(Transaction transaction) {
        String date = transaction.getCreatedAt() == null
                ? null
                : transaction.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return new TransactionDTO(
                transaction.getId(),
                transaction.getInventoryId(),
                transaction.getPerson(),
                transaction.getItemName(),
                transaction.getItemCount(),
                transaction.getCategory(),
                transaction.getType(),
                transaction.getAmount(),
                date
        );
    }

    public static Transaction DTOToDO(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setInventoryId(transactionDTO.getInventoryId());
        transaction.setPerson(transactionDTO.getPerson());
        transaction.setItemName(transactionDTO.getItemName());
        transaction.setItemCount(transactionDTO.getItemCount());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setType(transactionDTO.getType());
        transaction.setAmount(transactionDTO.getAmount());
        return transaction;
    }
}
