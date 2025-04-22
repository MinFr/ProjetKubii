package com.pi4j.spring.boot.sample.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String showForm() {
        return "formulaire"; 
    }

@GetMapping("/scan")
public String showScanForm() {
    return "scan";
}

}


