package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.DO.Test;
import Hackathing.BackendTemplate.DTO.TestDTO;
import Hackathing.BackendTemplate.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author:   Hazim
 * Date:     22/4/2026
 * Time:     4:16 pm
 * Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping
        public Test add(@RequestBody TestDTO testDTO){
        return testService.add(testDTO);
    }

    @GetMapping
    public List<Test> getAll(){
        return testService.getAll();
    }

    @GetMapping("/id/{id}")
    public Test getById(@PathVariable long id){
        return testService.getById(id);
    }

    @GetMapping("{value}")
    public List<Test> getByValue(@PathVariable String value){
        return testService.getByValue(value);
    }

    @GetMapping("/valueAndNum")
    public List<Test> getByValueAndNum(@RequestParam String value, @RequestParam int num){
        return testService.getByValueAndNum(value, num);
    }

    @DeleteMapping
    public void deleteAll(){
        testService.deleteAll();
    }
}
