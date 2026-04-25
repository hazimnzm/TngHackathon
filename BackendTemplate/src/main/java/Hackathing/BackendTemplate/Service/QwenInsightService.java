package Hackathing.BackendTemplate.Service;
import Hackathing.BackendTemplate.DTO.MerchantData;

//Spring
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// OkHttp
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// Jackson
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Java standard
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QwenInsightService {

    private static final String QWEN_URL = 
        "https://dashscope-intl.aliyuncs.com/compatible-mode/v1/chat/completions";

    @Value("${qwen.api.key}")
    private String apiKey;

    private final OkHttpClient httpClient = new OkHttpClient();
    //private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectMapper objectMapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public String getInsight(String merchantQuestion, MerchantData data) throws Exception {
        if (merchantQuestion == null || merchantQuestion.isBlank()) {
            return "Please ask a question.";
        }
    
        // Build the system prompt with live merchant data injected
        String systemPrompt = buildSystemPrompt(data);

        // Build request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-plus");
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", systemPrompt),
            Map.of("role", "user",   "content", merchantQuestion)
        ));

        String jsonBody = objectMapper.writeValueAsString(requestBody);
        System.out.println("=== QWEN REQUEST BODY ===");
        System.out.println(jsonBody); // <-- add this
        System.out.println("=========================");


        // RequestBody body = RequestBody.create(
        //     objectMapper.writeValueAsString(requestBody),
        //     MediaType.get("application/json")
        // );
        RequestBody body = RequestBody.create(
            jsonBody,
            MediaType.get("application/json")
        );
    

        Request request = new Request.Builder()
            .url(QWEN_URL)
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println("=== QWEN RESPONSE ===");
            System.out.println(responseBody); // <-- add this
            System.out.println("=====================");

            JsonNode json = objectMapper.readTree(responseBody);
            return json.at("/choices/0/message/content").asText();
        }
    }

    private String buildSystemPrompt(MerchantData data) {
        return """
                You are a smart business advisor for micro-merchants in Malaysia using TNG QR payments.
                Analyze the merchant's inventory and transaction data, then give short, practical advice.
                Be friendly and direct.
                All amounts are in MYR.
    
                === MERCHANT DATA ===
                Total Items: %d
                
                Inventory:
                %s
                
                Recent Transactions (last 7 days):
                %s
                
                Top Selling Item: %s
                Low Stock Items: %s
                ===
                """.formatted(
                    data.getTotalItems(),
                    data.getInventoryJson()          != null ? data.getInventoryJson()          : "No inventory data",
                    data.getRecentTransactionJson() != null ? data.getRecentTransactionJson() : "No transactions yet",
                    data.getTopSellingItem()         != null ? data.getTopSellingItem()         : "N/A",
                    data.getLowStockItems()          != null ? data.getLowStockItems()          : "None"
                );
    }
}