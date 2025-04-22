package com.pi4j.spring.boot.sample.app.controller;

import com.pi4j.spring.boot.sample.app.service.Pi4JService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LedController {

    private final Pi4JService pi4JService;
    private static final Logger logger = LoggerFactory.getLogger(LedController.class);

    private static final Set<Integer> ALLOWED_PINS = Set.of(4, 5, 6, 7, 8, 12, 13, 16, 17, 20, 22, 23, 24);

    public LedController(Pi4JService pi4JService) {
        this.pi4JService = pi4JService;
    }

    @PostMapping("/gestionled")
    public String receiveForm(
            @RequestParam String code_barre,
            @RequestParam int quantite,
            @RequestParam String personne,
            @RequestParam String localisation,
            @RequestParam String nomproduit,
            @RequestParam int pin,
            RedirectAttributes redirectAttributes) {

        logger.info("📥 Formulaire reçu: code_barre={}, quantite={}, personne={}, localisation={}, nomproduit={}, pin={}",
                code_barre, quantite, personne, localisation, nomproduit, pin);

        if (ALLOWED_PINS.contains(pin)) {
            pi4JService.configureLed(pin);
            pi4JService.blinkLed(pin);
            logger.info("💡 LED sur pin {} clignote pour {}", pin, personne);
        } else {
            logger.warn("PIN {} non autorisé", pin);
        }

        redirectAttributes.addFlashAttribute("code_barre", code_barre);
        redirectAttributes.addFlashAttribute("quantite", quantite);
        redirectAttributes.addFlashAttribute("personne", personne);
        redirectAttributes.addFlashAttribute("localisation", localisation);
        redirectAttributes.addFlashAttribute("nomproduit", nomproduit);
        redirectAttributes.addFlashAttribute("pin", pin);

        return "redirect:/";
    }
}




