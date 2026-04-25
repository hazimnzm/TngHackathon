package Hackathing.BackendTemplate.DTO;

import lombok.Data;

@Data
public class TransactionProfitLossRequest {
    private long inventoryId;
    private String timeFilter = "day";
    private String typeFilter;
}
