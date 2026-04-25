package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.DTO.TransactionListResponse;
import Hackathing.BackendTemplate.DTO.TransactionProfitLossRequest;
import Hackathing.BackendTemplate.DTO.TransactionProfitLossResponse;
import Hackathing.BackendTemplate.DTO.TransactionQueryRequest;
import Hackathing.BackendTemplate.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/query")
    public TransactionListResponse getTransaction(@RequestBody TransactionQueryRequest request) {
        return new TransactionListResponse(
                transactionService.getTransaction(
                        request.getInventoryId(),
                        request.getTimeFilter(),
                        request.getDisplacement(),
                        request.getTypeFilter()
                )
        );
    }

    @PostMapping("/profit-loss")
    public TransactionProfitLossResponse getProfitLoss(@RequestBody TransactionProfitLossRequest request) {
        return new TransactionProfitLossResponse(
                transactionService.getProfitLoss(
                        request.getInventoryId(),
                        request.getTimeFilter(),
                        request.getTypeFilter()
                )
        );
    }

    @GetMapping("/{inventoryId}/total-pl")
    public Double getTotalPL(@PathVariable long inventoryId) {
        return transactionService.getTotalNetProfitLoss(inventoryId);
    }
}
