package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Test;
import Hackathing.BackendTemplate.DTO.TestDTO;
import Hackathing.BackendTemplate.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:   Hazim
 * Date:     22/4/2026
 * Time:     4:55 pm
 * Description:
 */
@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public Test add(TestDTO testDTO){
        Test test = TestDTO.DTOToDO(testDTO);
        return testRepository.save(test);
    }

    public List<Test> getAll(){
        return testRepository.findAll();
    }

    public Test getById(long id){
        return testRepository.findById(id).orElse(null);
    }

    public List<Test> getByValue(String value){
        return testRepository.findByValue(value);
    }
    public void deleteAll(){
        testRepository.deleteAll();
    }

    public List<Test> getByValueAndNum(String value, int num){
        return testRepository.findByValueAndNum(value, num);
    }
}
