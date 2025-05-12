package com.pi4j.spring.boot.sample.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
    System.out.println("helleoworod================");
        SpringApplication.run(Application.class, args);

    }


}