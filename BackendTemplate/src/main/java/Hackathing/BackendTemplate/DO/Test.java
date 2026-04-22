package Hackathing.BackendTemplate.DO;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author:   Hazim
 * Date:     22/4/2026
 * Time:     9:24 pm
 * Description:
 */

@Data
@Entity
@Table(name = "Test")
@EqualsAndHashCode(callSuper = true)
public class Test extends DO{
    private String value;
    private int num;
}
