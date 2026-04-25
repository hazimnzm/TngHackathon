package Hackathing.BackendTemplate.DO;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "transactions")
@EqualsAndHashCode(callSuper = true)
public class Transaction extends DO {
    private long inventoryId;
    private String person;
    private String itemName;
    private long itemCount;
    private String category;
    private String type;
    private double amount;
}
