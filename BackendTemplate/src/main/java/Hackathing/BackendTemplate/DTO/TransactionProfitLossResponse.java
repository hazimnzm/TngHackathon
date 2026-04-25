package Hackathing.BackendTemplate.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TransactionProfitLossResponse {
    private List<TransactionGroupDTO> groups;
}
