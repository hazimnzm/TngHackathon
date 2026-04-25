package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Inventory;
import Hackathing.BackendTemplate.DO.Item;
import Hackathing.BackendTemplate.DTO.ItemUpdateRequest;
import Hackathing.BackendTemplate.DTO.InventoryDTO;
import Hackathing.BackendTemplate.Repository.InventoryRepository;
import Hackathing.BackendTemplate.Repository.ItemRepository;
import Hackathing.BackendTemplate.Repository.MerchantRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    public Inventory createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = InventoryDTO.DTOToDO(inventoryDTO);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(long inventoryId) {
        itemRepository.deleteByInventoryId(inventoryId);
        inventoryRepository.deleteById(inventoryId);
    }


    public List<Item> getItemsForCurrentMerchant() {
        long merchantId = requireCurrentMerchantId();
        List<Long> inventoryIds = inventoryRepository.findByMerchantId(merchantId)
                .stream()
                .map(Inventory::getId)
                .toList();

        if (inventoryIds.isEmpty()) {
            return List.of();
        }

        return itemRepository.findByInventoryIdIn(inventoryIds);
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
}
