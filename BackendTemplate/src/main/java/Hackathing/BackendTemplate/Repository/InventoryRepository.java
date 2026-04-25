package Hackathing.BackendTemplate.Repository;

import Hackathing.BackendTemplate.DO.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByMerchantId(long merchantId);
    void deleteByMerchantId(long merchantId);
}
