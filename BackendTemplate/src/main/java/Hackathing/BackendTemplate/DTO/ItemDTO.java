package Hackathing.BackendTemplate.DTO;

import Hackathing.BackendTemplate.DO.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDTO {
    private String name;
    private String url;
    private double count;
    private double price;
    private long inventoryId;

    public static ItemDTO DOToDTO(Item item) {
        return new ItemDTO(
                item.getName(),
                item.getUrl(),
                item.getCount(),
                item.getPrice(),
                item.getInventoryId()
        );
    }

    public static Item DTOToDO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setUrl(itemDTO.getUrl());
        item.setCount(itemDTO.getCount());
        item.setPrice(itemDTO.getPrice());
        item.setInventoryId(itemDTO.getInventoryId());
        return item;
    }
}
