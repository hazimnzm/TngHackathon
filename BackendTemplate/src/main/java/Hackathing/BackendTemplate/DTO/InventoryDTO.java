package Hackathing.BackendTemplate.DTO;

import Hackathing.BackendTemplate.DO.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InventoryDTO {
    private long merchantId;
    private Long id;
    private List<ItemDTO> items;

    public static InventoryDTO DOToDTO(Inventory inventory) {
        return new InventoryDTO(inventory.getMerchantId(), inventory.getId(), null);
    }

    public static Inventory DTOToDO(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        inventory.setMerchantId(inventoryDTO.getMerchantId());
        return inventory;
    }
}
