package com.pi4j.spring.boot.sample.app.controller;


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
import com.pi4j.spring.boot.sample.app.model.Commande;
import com.pi4j.spring.boot.sample.app.model.CommandeRequest;
import com.pi4j.spring.boot.sample.app.service.CommandeService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommandeController {


    private final CommandeService commandeService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${mock.api.url:http://localhost/api/kubii/commande}")
    private String apiUrl;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }


    @PostMapping("/retour_bonlivraison")
    public ResponseEntity<String> postBonLivraison(@RequestBody CommandeRequest request, HttpServletRequest servletRequest) throws JsonProcessingException {
    
        System.out.println("Request Method: " + servletRequest.getMethod());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Request Data: " + mapper.writeValueAsString(request));

   
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", request.getBon_livraison());
     
        HttpEntity<CommandeRequest> entity = new HttpEntity<>(request, headers);
     
        ResponseEntity<Commande> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Commande.class);
    
        Commande responseBody = response.getBody();
        if (responseBody == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: Réponse vide de l'API Kubii (Mockoon)");
        }

        Commande saved = commandeService.saveOrUpdate(responseBody);
      
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-From-Spring", "True");
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Colis reçu et stocké : " + saved.getBon_livraison());
    }


    @GetMapping("/commande/{bonLivraison}")
    public ResponseEntity<?> getCommande(@PathVariable String bonLivraison) {
        Optional<Commande> commande = commandeService.findByIdColis(bonLivraison);

        if (commande.isPresent()) {
            return ResponseEntity.ok(commande.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Commande non trouvée");
        }
    }
}

