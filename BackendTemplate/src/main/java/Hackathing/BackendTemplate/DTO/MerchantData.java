package Hackathing.BackendTemplate.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantData {
    //private String merchantName;
//    private int totalItems;
    private String inventoryJson;    // your existing inventory serialized
    private String recentTransactionJson;  // QR scan logs aggregated by day
    private String topSellingItem;
    private String lowStockItems;
}
