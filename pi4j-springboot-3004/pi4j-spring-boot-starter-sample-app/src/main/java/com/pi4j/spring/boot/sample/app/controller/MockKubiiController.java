package com.pi4j.spring.boot.sample.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock-api")
public class MockKubiiController {

    @PostMapping("/colis")
    public ResponseEntity<Map<String, Object>> receiveColis(@RequestBody Map<String, Object> request) {
        String idColis = (String) request.get("id_colis");

        Map<String, Object> response = new HashMap<>();
        response.put("id_colis", idColis);
        response.put("client", "Mickael Cormier");
        response.put("etat_colis", "cree");
        response.put("date_creation", "2025-04-28");
        response.put("id_product", "4587");
        response.put("ean13_product", "5056561804484");
        response.put("name_product", "Kit Raspberry Pi 500 (Version : Française (FR))");
        response.put("qty", "1");
        response.put("qte_scannee", "0");
        response.put("Emp_code", "L2-N1-E1");

        return ResponseEntity.ok(response);
    }
}


