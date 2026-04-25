package Hackathing.BackendTemplate.Repository;

import Hackathing.BackendTemplate.DO.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByName(String name);
    List<Item> findByInventoryId(long inventoryId);
    void deleteByInventoryId(long inventoryId);
}
