package com.pi4j.spring.boot.sample.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.pi4j.spring.boot.sample.app.service.CsvProduitService;
import com.pi4j.spring.boot.sample.app.service.Pi4JService;
import com.pi4j.spring.boot.sample.app.service.Produit;

@Controller
public class ProduitController {
    @Autowired
    private CsvProduitService produitService;
    @Autowired
    private Pi4JService pi4jService;

    @PostMapping("/scan")
    public String handleScan(@RequestParam String code_barre, Model model) {
        Optional<Produit> produitOpt = produitService.findByEan(code_barre);

        if (produitOpt.isPresent()) {
            Produit produit = produitOpt.get();
            int pin = extractPinFromGpioName(produit.getBoitierAClignoter());
            pi4jService.configureLed(pin);
            pi4jService.blinkLed(pin);
            model.addAttribute("produit", produit);
        } else {
            model.addAttribute("error", "Produit introuvable");
        }

        return "produit";
    }

    private int extractPinFromGpioName(String gpioName) {
        return Integer.parseInt(gpioName.replaceAll("\\D+", ""));
    }
}


