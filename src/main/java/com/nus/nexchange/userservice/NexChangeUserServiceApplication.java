package com.nus.nexchange.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nus.nexchange.userservice"})
public class NexChangeUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexChangeUserServiceApplication.class, args);
    }

}
