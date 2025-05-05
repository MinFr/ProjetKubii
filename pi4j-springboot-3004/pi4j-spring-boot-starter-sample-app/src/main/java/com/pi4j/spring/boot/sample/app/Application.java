package com.pi4j.spring.boot.sample.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    // Expose with ngrok: `ngrok http --domain=pi4j.ngrok.dev 8080`

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

     @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
