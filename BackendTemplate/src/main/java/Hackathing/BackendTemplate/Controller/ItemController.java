package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.DO.Item;
import Hackathing.BackendTemplate.DTO.ItemDTO;
import Hackathing.BackendTemplate.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

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
}
