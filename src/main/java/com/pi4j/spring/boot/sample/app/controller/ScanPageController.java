package com.pi4j.spring.boot.sample.app.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ScanPageController {


    @GetMapping("/scan")
    public String scanPage() {
        return "scan";
    }
}


