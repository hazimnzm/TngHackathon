package Hackathing.BackendTemplate.Repository;

import Hackathing.BackendTemplate.DO.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM Transaction t WHERE t.inventoryId = :inventoryId")
    Double sumAmountByInventoryId(@Param("inventoryId") long inventoryId);
}
