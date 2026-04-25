package Hackathing.BackendTemplate.Repository;

import Hackathing.BackendTemplate.DO.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByInventoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(
            long inventoryId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Transaction> findByInventoryIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(
            long inventoryId,
            String type,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Transaction> findByInventoryIdOrderByCreatedAtDesc(long inventoryId);
}
