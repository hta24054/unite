package com.hta2405.unite;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisConfigDebugger {

    @Value("${spring.data.redis.host:Not Found}")
    private String redisHost;

    @Value("${spring.data.redis.port:Not Found}")
    private String redisPort;

    @Value("${cloud.aws.region.static}")
    private String testAws;

    @PostConstruct
    public void debugRedisConfig() {
        System.out.println("==== Redis Configuration ====");
        System.out.println("Redis Host: " + redisHost);
        System.out.println("Redis Port: " + redisPort);
        System.out.println("testAws: " + testAws);
        System.out.println("=============================");
    }
}