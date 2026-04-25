package Hackathing.BackendTemplate.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TransactionGroupDTO {
    private String timeRange;
    private String type;
    private double totalAmount;
    private List<TransactionDTO> transactions;
}
