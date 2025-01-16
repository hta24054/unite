package com.hta2405.unite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UniteApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniteApplication.class, args);
    }
}
