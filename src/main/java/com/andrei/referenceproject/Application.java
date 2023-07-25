package com.andrei.referenceproject;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import javax.management.ObjectInstance;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
        ApplicationContext context = new SpringApplicationBuilder(Application.class)
                .headless(false)
                .run(args);
        context.getId();
    }
}