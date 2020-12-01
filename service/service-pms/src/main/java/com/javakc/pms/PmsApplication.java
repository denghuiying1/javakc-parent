package com.javakc.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.javakc"})
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients
public class PmsApplication {

    public static void main(String[] args) {

//        System.out.println(UUID.randomUUID());
//        System.out.println(UUID.randomUUID());
//        System.out.println(UUID.randomUUID());
//        System.out.println(UUID.randomUUID());
        SpringApplication.run(PmsApplication.class,args);
    }
}
