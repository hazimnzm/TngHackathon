package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.DO.Item;
import Hackathing.BackendTemplate.DTO.ItemDTO;
import Hackathing.BackendTemplate.DTO.PurchaseItemRequest;
import Hackathing.BackendTemplate.DTO.RestockItemRequest;
import Hackathing.BackendTemplate.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public Item createItem(@RequestBody ItemDTO itemDTO) {
        return itemService.createItem(itemDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }

    @PatchMapping("/{id}/quantity")
    public Item updateQuantity(@PathVariable long id, @RequestParam double count) {
        return itemService.updateQuantity(id, count);
    }

    @PostMapping("/purchase")
    public Item purchaseItem(@RequestBody PurchaseItemRequest request) {
        return itemService.purchaseItem(request.getId(), request.getPerson(), request.getCount());
    }

    @PostMapping("/restock")
    public Item restockItem(@RequestBody RestockItemRequest request) {
        return itemService.restockItem(request.getId(), request.getPerson(), request.getCount(), request.getCostPerUnit());
    }
}
