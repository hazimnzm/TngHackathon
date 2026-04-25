package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Inventory;
import Hackathing.BackendTemplate.DO.Item;
import Hackathing.BackendTemplate.DO.Merchant;
import Hackathing.BackendTemplate.DTO.ItemDTO;
import Hackathing.BackendTemplate.DTO.ItemUpdateRequest;
import Hackathing.BackendTemplate.DTO.InventoryDTO;
import Hackathing.BackendTemplate.DTO.MerchantData;
import Hackathing.BackendTemplate.Repository.InventoryRepository;
import Hackathing.BackendTemplate.Repository.ItemRepository;
import Hackathing.BackendTemplate.Repository.MerchantRepository;
//import jakarta.transaction.Transaction;
import Hackathing.BackendTemplate.DO.Transaction;
import Hackathing.BackendTemplate.Repository.TransactionRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    //private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectMapper objectMapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public Inventory createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = InventoryDTO.DTOToDO(inventoryDTO);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(long inventoryId) {
        itemRepository.deleteByInventoryId(inventoryId);
        inventoryRepository.deleteById(inventoryId);
    }


    public InventoryDTO getInventoryForCurrentMerchantInventory(long inventoryId) {
        long merchantId = requireCurrentMerchantId();
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        if (!Long.valueOf(inventory.getMerchantId()).equals(merchantId)) {
            throw new IllegalArgumentException("Inventory does not belong to current merchant");
        }
        InventoryDTO inventoryDTO = InventoryDTO.DOToDTO(inventory);
        List<Item> items = itemRepository.findByInventoryId(inventoryId);
        String merchantName = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"))
                .getDisplayName();
        inventoryDTO.setItems(items.stream().map(ItemDTO::DOToDTO).toList());
        inventoryDTO.setMerchantName(merchantName);
        return inventoryDTO;
    }

    @Transactional
    public Item updateItemDetailsForCurrentMerchant(long itemId, ItemUpdateRequest req) {
        long merchantId = requireCurrentMerchantId();

        // fetch item  
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        // fetch inventory  
        Inventory inv = inventoryRepository.findById(item.getInventoryId())
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));

        // if (inv.getMerchantId() != merchantId) {
        //     throw new IllegalArgumentException("Item does not belong to current merchant");
        // }
        if (!Long.valueOf(inv.getMerchantId()).equals(merchantId)) {
            throw new IllegalArgumentException("Item does not belong to current merchant");
        }

        // if merchant only sends a new price, the name and count remains the same in database
        if (req.getName() != null) {
            item.setName(req.getName());
        }
        if (req.getPrice() != null) {
            item.setPrice(req.getPrice());
        }

        return itemRepository.save(item);
    }

    // helper method
    //the frontend doesnt tell the backend who the merchant is, it just sends the token
    private long requireCurrentMerchantId() {
        // securityContextHolder = reach into the pocket of current request, it stuff the merchant's email into this pocket after token validated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new IllegalArgumentException("Not authenticated");
        }
    
        // retrieve the email from the  token
        String email = auth.getName();
        // database lookup = goes to MerchantRepository to find the uniqueID associated with the email
        return merchantRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"))
                .getId();
    }

    //assembling a snapshot of merchant business data to feed into QWEN
    public MerchantData getMerchantSnapshot() throws Exception {

        // reuse your existing helper — reads email from JWT token
        long merchantId = requireCurrentMerchantId();

        // get merchant details for the name
        Merchant merchant = merchantRepository.findByEmail(
            SecurityContextHolder.getContext().getAuthentication().getName().toLowerCase().trim()
        ).orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        // get all inventories for this merchant
        List<Inventory> inventories = inventoryRepository.findByMerchantId(merchantId);

        // get transactions from last 7 days
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Transaction> recentTransactions = transactionRepository
            .findByInventory_MerchantIdAndCreatedAtAfter(merchantId, sevenDaysAgo);

        // find top selling item
        String topSellingItem = recentTransactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::getItemName,
                Collectors.summingLong(Transaction::getItemCount)
            ))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");

        // find low stock items
        String lowStockItems = recentTransactions.stream()
            .filter(t -> t.getItemCount() != null && t.getItemCount() <= 5)
            .map(Transaction::getItemName)
            .distinct()
            .collect(Collectors.joining(", "));

        return MerchantData.builder()
            .totalItems(inventories.size())
            .inventoryJson(objectMapper.writeValueAsString(inventories))
            .recentTransactionJson(objectMapper.writeValueAsString(recentTransactions))
            .topSellingItem(topSellingItem)
            .lowStockItems(lowStockItems.isEmpty() ? "None" : lowStockItems)
            .build();
    }
}
