package com.worksap.stm2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication {
    public static void main(String[] args) {
    	System.out.println(StringUtils.isEmpty(""));
    	System.out.println(StringUtils.isEmpty(" "));
    	System.out.println(StringUtils.isEmpty(null));
    	SpringApplication.run(SampleApplication.class, args);
    }
    

    
}
                                    


