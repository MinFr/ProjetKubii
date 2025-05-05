package com.pi4j.spring.boot.sample.app.controller;


import org.springframework.web.bind.annotation.*; 
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.pi4j.spring.boot.sample.app.service.ColisService;
import com.pi4j.spring.boot.sample.app.service.KubiiApiService;

@RestController
@RequestMapping("/scan")
public class ScanController {

    private final KubiiApiService kubiiApiService;

    public ScanController(KubiiApiService kubiiApiService) {
        this.kubiiApiService = kubiiApiService;
    }

    @GetMapping("/colis/{id}")
    public ResponseEntity<ColisService> scanColis(@PathVariable("id") String idColis) {
        ColisService colis = kubiiApiService.envoyerColis(idColis);
        return ResponseEntity.ok(colis);
    }
}




