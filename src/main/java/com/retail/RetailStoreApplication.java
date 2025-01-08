package com.retail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RetailStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailStoreApplication.class, args);
    }

}
