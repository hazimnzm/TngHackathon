package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Inventory;
import Hackathing.BackendTemplate.DTO.InventoryDTO;
import Hackathing.BackendTemplate.Repository.InventoryRepository;
import Hackathing.BackendTemplate.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ItemRepository itemRepository;

    public Inventory createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = InventoryDTO.DTOToDO(inventoryDTO);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(long inventoryId) {
        itemRepository.deleteByInventoryId(inventoryId);
        inventoryRepository.deleteById(inventoryId);
    }
}
