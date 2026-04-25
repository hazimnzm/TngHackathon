package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Item;
import Hackathing.BackendTemplate.DTO.ItemDTO;
import Hackathing.BackendTemplate.Repository.InventoryRepository;
import Hackathing.BackendTemplate.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private TransactionService transactionService;

    public Item createItem(ItemDTO itemDTO) {
        Item item = ItemDTO.DTOToDO(itemDTO);
        Item savedItem = itemRepository.save(item);
        return itemRepository.save(savedItem);
    }

    public void deleteItem(long id) {
        itemRepository.deleteById(id);
    }

    public Item updateQuantity(long id, double count) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        item.setCount(count);
        return itemRepository.save(item);
    }

    @Transactional
    public Item purchaseItem(long id, String person, long count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }

        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("item not found"));
        if (item.getCount() < count) {
            throw new IllegalArgumentException("insufficient item count");
        }

        long inventoryId = inventoryRepository.findById(item.getInventoryId())
                .orElseThrow(() -> new IllegalArgumentException("inventory not found"))
                .getId();

        item.setCount(item.getCount() - count);
        Item updatedItem = itemRepository.save(item);

        double amount = count * item.getPrice();
        transactionService.addTransaction(
                inventoryId,
                person,
                item.getName(),
                count,
                item.getCategory(),
                item.getCategory(),
                amount
        );
        return updatedItem;
    }

    @Transactional
    public Item restockItem(long id, String person, long count, double costPerUnit) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }
        if (costPerUnit < 0) {
            throw new IllegalArgumentException("costPerUnit must be 0 or greater");
        }

        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("item not found"));
        long inventoryId = inventoryRepository.findById(item.getInventoryId())
                .orElseThrow(() -> new IllegalArgumentException("inventory not found"))
                .getId();

        item.setCount(item.getCount() + count);
        Item updatedItem = itemRepository.save(item);

        double netCost = count * costPerUnit;
        transactionService.addTransaction(
                inventoryId,
                person,
                item.getName(),
                count,
                item.getCategory(),
                item.getCategory(),
                -netCost
        );
        return updatedItem;
    }
}
