package Hackathing.BackendTemplate.DTO;

import Hackathing.BackendTemplate.DO.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDTO {
    private String name;
    private double count;
    private double price;
    private long inventoryId;
    private Long id;

    public static ItemDTO DOToDTO(Item item) {
        return new ItemDTO(
                item.getName(),
                item.getCount(),
                item.getPrice(),
                item.getInventoryId(),
                item.getId()
        );
    }

    public static Item DTOToDO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setCount(itemDTO.getCount());
        item.setPrice(itemDTO.getPrice());
        item.setInventoryId(itemDTO.getInventoryId());
        return item;
    }
}
