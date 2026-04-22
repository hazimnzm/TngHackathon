package Hackathing.BackendTemplate.DO;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Author:   Hazim
 * Date:     22/4/2026
 * Time:     4:41 pm
 * Description:
 */

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

}
