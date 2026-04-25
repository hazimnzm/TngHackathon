package Hackathing.BackendTemplate.DTO;

import lombok.Data;

@Data
public class TransactionQueryRequest {
    private long inventoryId;
    private String timeFilter = "day";
    private int displacement = 0;
    private String typeFilter;
}
