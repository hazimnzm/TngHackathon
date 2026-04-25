package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.DO.Inventory;
import Hackathing.BackendTemplate.DTO.InventoryDTO;
import Hackathing.BackendTemplate.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public Inventory createInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.createInventory(inventoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteInventory(@PathVariable long id) {
        inventoryService.deleteInventory(id);
    }
    @GetMapping("/test")
    public String test() {
      return "Inventory path is reachable!";
}
}
