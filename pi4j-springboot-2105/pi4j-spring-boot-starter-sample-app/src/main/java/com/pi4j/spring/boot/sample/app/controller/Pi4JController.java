package com.pi4j.spring.boot.sample.app.controller;

import com.pi4j.spring.boot.sample.app.service.Pi4JService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
@RequestMapping("/")
public class Pi4JController {

    private final Pi4JService pi4JService;

    public Pi4JController(@Autowired Pi4JService pi4JService) {
        this.pi4JService = pi4JService;
        
    }

  @GetMapping("/led1/clignote")
     public String blinkLed1() {
        pi4JService.blinkColor("white");

    return "LED1 se clignote pendant 5 seconde ========";
    
}



}
