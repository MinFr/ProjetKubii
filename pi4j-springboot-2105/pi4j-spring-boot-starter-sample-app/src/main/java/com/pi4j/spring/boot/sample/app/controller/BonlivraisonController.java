package com.pi4j.spring.boot.sample.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;
import com.pi4j.spring.boot.sample.app.model.BonlivraisonRequest;
import com.pi4j.spring.boot.sample.app.component.CagetteAllouer;
import com.pi4j.spring.boot.sample.app.component.ActiveCommande;

import jakarta.servlet.http.HttpServletRequest;


@RequestMapping("/api")
@RestController
public class BonlivraisonController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final CagetteAllouer cagetteAllouer;
    private final ActiveCommande activeCommande;

    @Value("${mock.api.url:http://192.168.68.53:3000/api/kubii/commande}")
    private String apiUrl;

    public BonlivraisonController(CagetteAllouer cagetteAllouer, ActiveCommande activeCommande) {
        this.cagetteAllouer = cagetteAllouer;
        this.activeCommande = activeCommande;
    }

    @PostMapping("/retour_bonlivraison")
    public ResponseEntity<String> postBonLivraison(@RequestBody BonlivraisonRequest request, HttpServletRequest servletRequest) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        System.out.println("HTTP Method: " + servletRequest.getMethod());
        System.out.println("JSON Request: " + mapper.writeValueAsString(request));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", request.getBon_livraison());

        HttpEntity<BonlivraisonRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Bonlivraison> response = restTemplate.exchange(
            apiUrl, HttpMethod.POST, entity, Bonlivraison.class
        );

        Bonlivraison json_body = response.getBody();

        if (json_body == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: Réponse vide de l'API");
        }

        if (activeCommande.containsBon(json_body.getBon_livraison())) {
            System.out.println("ce bl est inutile");
            return ResponseEntity.status(409).body("❗Ce BL est déjà actif, inutile de le recharger.");
        }

        Optional<Integer> maybeCagette = cagetteAllouer.allocateRandom();
        if (maybeCagette.isEmpty()) {
            return ResponseEntity.ok("Toutes les cagettes sont déjà utilisées. BL ignoré.");
        }
        int numeroCagette = maybeCagette.get();

        json_body.setNumero_cagette_alloue(numeroCagette);


        json_body.setId_operateur(null);
        json_body.setId_controller(null);

        System.out.println("================Json===============\n" +
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json_body));

        Optional<Integer> result = activeCommande.register(json_body, numeroCagette);
        if (result.isEmpty()) {
            return ResponseEntity.ok("ℹ Bon de livraison ignoré : limite atteinte.");
        }

        System.out.println("====Json enregistré dans le map");

        return ResponseEntity.ok("Bonlivraison enregistrée pour BL: " + json_body.getBon_livraison());
    }
}
