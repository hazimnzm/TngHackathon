package Hackathing.BackendTemplate.DTO;
import lombok.Data;

@Data
public class ChatRequest {
    private String message;

    public String getMessage() {  // needs this
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
