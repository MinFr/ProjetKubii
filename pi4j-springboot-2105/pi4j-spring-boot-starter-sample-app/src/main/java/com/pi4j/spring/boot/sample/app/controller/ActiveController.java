package com.pi4j.spring.boot.sample.app.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.spring.boot.sample.app.component.ActiveCommande;
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;

@RestController
@RequestMapping("/api")
public class ActiveController {

    private final ActiveCommande activeCommande;

    public ActiveController(ActiveCommande activeCommande) {
        this.activeCommande = activeCommande;
    }

    @GetMapping("/active")
    public Map<Integer, Bonlivraison> getAllActive() {
        return activeCommande.getAll();
    }
}
