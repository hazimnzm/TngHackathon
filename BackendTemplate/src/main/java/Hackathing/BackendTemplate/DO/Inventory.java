package Hackathing.BackendTemplate.DO;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "inventories")
@EqualsAndHashCode(callSuper = true)
public class Inventory extends DO {
    long merchantId;
}
