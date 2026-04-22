package Hackathing.BackendTemplate.DTO;

import Hackathing.BackendTemplate.DO.Test;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author:   Hazim
 * Date:     22/4/2026
 * Time:     4:56 pm
 * Description:
 */
@Data
@AllArgsConstructor
public class TestDTO {
    private String value;
    private int num;

    public static TestDTO DOToDTO(Test test){
        return new TestDTO(test.getValue(), test.getNum());
    }

    public static Test DTOToDO(TestDTO testDTO){
        Test test = new Test();
        test.setValue(testDTO.getValue());
        test.setNum(testDTO.getNum());
        return test;
    }
}
