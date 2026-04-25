package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.Service.InventoryService;
import Hackathing.BackendTemplate.Service.QwenInsightService;
import Hackathing.BackendTemplate.DTO.MerchantData;
import Hackathing.BackendTemplate.DTO.ChatRequest;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// Java standard
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private QwenInsightService qwenService;

    @Autowired
    private InventoryService inventoryService; // your existing service

    @PostMapping("/insight")
    public ResponseEntity<Map<String, String>> getInsight(
        @RequestBody ChatRequest request
    ) throws Exception {
        // Pull live data from existing inventory system
        MerchantData data = inventoryService.getMerchantSnapshot(); //no argument
        String answer = qwenService.getInsight(request.getMessage(), data);
        return ResponseEntity.ok(Map.of("reply", answer));
    }
}