package com.hta2405.unite;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextDebugger {

    @Value("${secret-properties.test}")
    private String secretTest;
    @Value("${normal-properties.test}")
    private String normalTest;


    @PostConstruct
    public void debugRedisConfig() {
        System.out.println("==== properties.test ====");
        System.out.println("secret test = " + secretTest);
        System.out.println("normal test = " + normalTest);
        System.out.println("=============================");
    }
}