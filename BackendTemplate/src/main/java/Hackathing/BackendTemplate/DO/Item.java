package Hackathing.BackendTemplate.DO;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "Item")
@EqualsAndHashCode(callSuper = true)
public class Item extends DO {
    private String name;
    private String url;
    private double count;
    private double price;
    private long inventoryId;
}
