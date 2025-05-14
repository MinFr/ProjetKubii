package com.pi4j.spring.boot.sample.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.spring.boot.sample.app.model.ScanRequest;
import com.pi4j.spring.boot.sample.app.service.CouchDbService;

@RestController
@RequestMapping("/api")
public class ScanController {

    private final CouchDbService couchDbService;

    public ScanController(CouchDbService couchDbService) {
        this.couchDbService = couchDbService;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> scan(@RequestBody ScanRequest request) {
        if (request == null || request.getEan13() == null || request.getEan13().isEmpty()) {
            return ResponseEntity.badRequest().body("Missing or empty ean13");
        }


        boolean updated = couchDbService.updateArticleScanStatus(request.getEan13());
        if (updated) {
            return ResponseEntity.ok("EAN scanned and updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EAN not found in any commande");
        }
    }
}

