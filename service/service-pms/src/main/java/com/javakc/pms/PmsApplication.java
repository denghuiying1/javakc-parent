package com.javakc.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@SpringBootApplication
@ComponentScan(basePackages = {"com.javakc"})
@EnableJpaAuditing
public class PmsApplication {

    public static void main(String[] args) {

//        System.out.println(UUID.randomUUID());
//        System.out.println(UUID.randomUUID());
//        System.out.println(UUID.randomUUID());
//        System.out.println(UUID.randomUUID());
        SpringApplication.run(PmsApplication.class,args);
    }
}