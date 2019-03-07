package com.worksap.stm2017;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleApplication.class)
@WebAppConfiguration
public class SampleApplicationTests {

    @Test
    public void contextLoads() {
    }
    
    @Test
    public void testSubtract() {
        assertEquals(4, 4);
    }

}
