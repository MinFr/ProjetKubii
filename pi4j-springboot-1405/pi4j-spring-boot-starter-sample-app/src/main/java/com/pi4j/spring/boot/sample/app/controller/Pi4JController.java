package com.pi4j.spring.boot.sample.app.controller;

import com.pi4j.spring.boot.sample.app.service.Pi4JService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
@RequestMapping("/api/pi4j")
public class Pi4JController {

    private static final Logger logger = LoggerFactory.getLogger(Pi4JController.class);
    private final Pi4JService pi4JService;

    public Pi4JController(@Autowired Pi4JService pi4JService) {
        this.pi4JService = pi4JService;
        
    }

    @GetMapping("/led/{state}")
    public Boolean setLedStatus(@PathVariable Boolean state) {
        logger.info("REST request to change the LED state to {}", state);
        return pi4JService.setLedState(22,state);
    }


  @GetMapping("/led1/clignote")
     public String blinkLed1() {
        logger.info("**********  REST request to blink LED1 for 5 seconds  ****************" );
    new Thread(() -> {
        int interval = 500;      
        int duration = 5000;     
        int cycles = duration / interval;

        for (int i = 0; i < cycles; i++) {
            try {
                pi4JService.setLedState(23,true);  
                Thread.sleep(interval / 2);

                pi4JService.setLedState(23,false); 
                Thread.sleep(interval / 2);

            } catch (InterruptedException e) {
                logger.error("Blink thread interrupted", e);
            }
        }

        pi4JService.setLedState(23,false); 
    }).start();

    return "LED1 se clignote pendant 5 seconde ========";
    
}



}
