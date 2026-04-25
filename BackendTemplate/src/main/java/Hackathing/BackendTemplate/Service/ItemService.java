package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Item;
import Hackathing.BackendTemplate.DTO.ItemDTO;
import Hackathing.BackendTemplate.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

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
}
