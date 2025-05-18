package com.cxlian.ProbabilityTest;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProbabilityTestApplicationTests {

    @Resource
    ProbabilityController probabilityController;

    @Test
    void contextLoads() {


        System.out.println(probabilityController.calculateProbability_1(
                new ProbabilityController.InputParams(400*160, 0, 0, 20, 20, false)
        ).getResult());

    }

    @Test
    void contextLoads_2() {
        long startTime = System.currentTimeMillis();


        System.out.println(probabilityController.calculateProbability_2(
                new ProbabilityController.InputParams(400, 0, 0, 0, 0, false)
        ).getResult());

        long endTime = System.currentTimeMillis();
        System.out.println("执行耗时：" + (endTime - startTime) + "ms");
    }

    @Test
    void contextLoads_3() {
        long startTime = System.currentTimeMillis();


        System.out.println(probabilityController.calculateProbability_3(
                new ProbabilityController.InputParams(600, 0, 0, 0, 0, false),
                6
        ).getResult());

        long endTime = System.currentTimeMillis();
        System.out.println("执行耗时：" + (endTime - startTime) + "ms");
    }

}
