package Hackathing.BackendTemplate.DTO;

import Hackathing.BackendTemplate.DO.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryDTO {
    private long merchantId;
    private long id;

    public static InventoryDTO DOToDTO(Inventory inventory) {
        return new InventoryDTO(inventory.getMerchantId(), inventory.getId());
    }

    public static Inventory DTOToDO(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        inventory.setMerchantId(inventoryDTO.getMerchantId());
        inventory.setId(inventoryDTO.getId());
        return inventory;
    }
}
