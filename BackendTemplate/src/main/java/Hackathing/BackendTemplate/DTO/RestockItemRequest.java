package Hackathing.BackendTemplate.DTO;

import lombok.Data;

@Data
public class RestockItemRequest {
    private long id;
    private String person;
    private long count;
    private double costPerUnit;
}
