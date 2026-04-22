package Hackathing.BackendTemplate.Repository;

import Hackathing.BackendTemplate.DO.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author:   Hazim
 * Date:     22/4/2026
 * Time:     10:01 pm
 * Description:
 */
public interface TestRepository extends JpaRepository<Test,Long> {
    public List<Test> findByValue(String value);
    public List<Test> findByValueAndNum(String value, int num);
}
