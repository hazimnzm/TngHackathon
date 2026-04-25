package Hackathing.BackendTemplate.DTO;

import lombok.Data;

@Data
public class ItemUpdateRequest {
    private String name;
    private Double count;
    private Double price;
}

