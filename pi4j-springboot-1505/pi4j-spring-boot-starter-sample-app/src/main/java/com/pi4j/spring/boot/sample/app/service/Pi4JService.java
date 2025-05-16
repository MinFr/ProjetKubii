package com.pi4j.spring.boot.sample.app.service;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Service;

@Service
public class Pi4JService implements InfoContributor {

    private static final Logger logger = LoggerFactory.getLogger(Pi4JService.class);

    private final Context pi4j;
    private final Map<Integer, DigitalOutput> leds = new ConcurrentHashMap<>();

    public Pi4JService(Context pi4j) {
        this.pi4j = pi4j;
        
        configureLed(4);
        configureLed(5);
        configureLed(6);
        configureLed(7);
        configureLed(8);
        configureLed(12); 
        configureLed(13);
        configureLed(16);
        configureLed(17);
        configureLed(20);
        configureLed(22);
        configureLed(23);
        configureLed(24);
        configureLed(27);

    }

    @Override
    public void contribute(Info.Builder builder) {
        leds.forEach((pin, led) -> builder.withDetail("pi4j.status.led." + pin, led.state()));
    }

    public boolean configureLed(int pin) {
        if (leds.containsKey(pin)) {
            return true;
        }

        try {
            DigitalOutput led = pi4j.digitalOutput().create(pin);
            leds.put(pin, led);
            return true;
        } catch (Exception e) {
            logger.error("Error while initializing the LED on pin {}: {}", pin, e.getMessage());
            return false;
        }
    }


    public boolean setLedState(int pin, boolean state) {
        DigitalOutput led = leds.get(pin);
        if (led == null) {
            logger.error("LED on pin {} is not configured.", pin);
            return false;
        }

        try {
            led.state(state ? DigitalState.HIGH : DigitalState.LOW);
            return true;
        } catch (Exception e) {
            logger.error("Error setting LED state on pin {}: {}", pin, e.getMessage());
            return false;
        }
    }

    public void blinkLed(int pin) {

        new Thread(() -> {
            int interval = 500;
            int duration = 5000;
            int cycles = duration / interval;

            for (int i = 0; i < cycles; i++) {
                try {
                    setLedState(pin, true);
                    Thread.sleep(interval / 2);
                    setLedState(pin, false);
                    Thread.sleep(interval / 2);
                } catch (InterruptedException e) {
                    logger.error("Blink thread interrupted on pin {}", pin);
                }
            }

            setLedState(pin, false);
        }).start();
    }

    public void setColor(String color) {
        color = color.toLowerCase();
        switch (color) {
            case "red":
            setLedState(22, true);
            setLedState(17, false);
            setLedState(27, false);
                break;
            case "green":
            setLedState(22, false);
            setLedState(17, true);
            setLedState(27, false);
                break;
            case "blue":
            setLedState(22, false);
            setLedState(17, false);
            setLedState(27, true);
                break;
            case "yellow":
            setLedState(22, true);
            setLedState(17, true);
            setLedState(27, false);
                break;
            case "purple":
            setLedState(22, true);
            setLedState(17, false);
            setLedState(27, true);
                break;
            case "white":
            setLedState(22, true);
            setLedState(17, true);
            setLedState(27, true);
                break;
            case "off":
            setLedState(22, false);
            setLedState(17, false);
            setLedState(27, false);
                break;
            default:
                logger.warn("Unknown color: {}", color);
        }
    }

    public void blinkColor(String color) {
        logger.info("Blinking color: {}", color);
    
    
        new Thread(() -> {
            int interval = 500;
            int duration = 5000;
            int cycles = duration / interval;
    
    
            for (int i = 0; i < cycles; i++) {
                // ON
                setColor(color);
                try {
                    Thread.sleep(interval / 2);
                } catch (InterruptedException e) {
                    logger.error("Interrupted while blinking ON phase");
                }
    
    
                // OFF
                setColor("off");
                try {
                    Thread.sleep(interval / 2);
                } catch (InterruptedException e) {
                    logger.error("Interrupted while blinking OFF phase");
                }
            }
    
            setColor("off");}).start();
    }
    


    
    
}

