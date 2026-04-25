package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.DO.Inventory;
import Hackathing.BackendTemplate.DO.Item;
import Hackathing.BackendTemplate.DTO.ItemUpdateRequest;
import Hackathing.BackendTemplate.DTO.InventoryDTO;
import Hackathing.BackendTemplate.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    //post 
    @PostMapping
    public Inventory createInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.createInventory(inventoryDTO);
    }

    //delete inventory
    @DeleteMapping("/{id}")
    public void deleteInventory(@PathVariable long id) {
        inventoryService.deleteInventory(id);
    }

    // get all items for currently logged-in merchant //merchant's inventory
    @GetMapping("/my/items/{inventoryId}")
    public List<Item> getMyItems(@PathVariable long inventoryId) {
        try {
            return inventoryService.getItemsForCurrentMerchantInventory(inventoryId);
        } catch (IllegalArgumentException e) {
            HttpStatus status = "Not authenticated".equalsIgnoreCase(e.getMessage())
                    ? HttpStatus.UNAUTHORIZED
                    : ("Inventory not found".equalsIgnoreCase(e.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN);
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    // update item details for currently logged-in merchant
    //every merchant can only update their own items
    @PutMapping("/my/items/{itemId}")
    public Item updateMyItem(@PathVariable long itemId, @RequestBody ItemUpdateRequest req) {
        try {
            return inventoryService.updateItemDetailsForCurrentMerchant(itemId, req);
        } catch (IllegalArgumentException e) {
            HttpStatus status = "Item not found".equalsIgnoreCase(e.getMessage()) ? HttpStatus.NOT_FOUND
                    : ("Not authenticated".equalsIgnoreCase(e.getMessage()) ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN);
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test() {
      return "Inventory path is reachable!";
}
}
