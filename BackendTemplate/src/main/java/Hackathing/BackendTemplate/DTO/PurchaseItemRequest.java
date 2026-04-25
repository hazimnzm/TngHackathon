package Hackathing.BackendTemplate.DTO;

import lombok.Data;

@Data
public class PurchaseItemRequest {
    private long id;
    private String person;
    private long count;
}
