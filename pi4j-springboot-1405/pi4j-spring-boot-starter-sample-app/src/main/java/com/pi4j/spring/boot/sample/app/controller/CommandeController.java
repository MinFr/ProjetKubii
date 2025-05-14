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
import com.pi4j.spring.boot.sample.app.service.CouchDbService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;



@RestController
@RequestMapping("/api")
public class CommandeController {


    private final CouchDbService couchDbService;
    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${mock.api.url:http://192.168.68.51:3000/api/kubii/commande}")
    private String apiUrl;

    public CommandeController(CouchDbService couchDbService) {
        this.couchDbService = couchDbService;
    }


    @PostMapping("/retour_bonlivraison")
    public ResponseEntity<String> postBonLivraison(@RequestBody CommandeRequest request, HttpServletRequest servletRequest) throws JsonProcessingException {
        System.out.println("==================conexion couch db========");
        ObjectMapper mapper = new ObjectMapper();


        System.out.println(" HTTP Method: " + servletRequest.getMethod());
        System.out.println(" JSON Request: " + mapper.writeValueAsString(request));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-ID", request.getBon_livraison());


        HttpEntity<CommandeRequest> entity = new HttpEntity<>(request, headers);


        ResponseEntity<Commande> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, entity, Commande.class);


        Commande body = response.getBody();


        if (body == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: Réponse vide de l'API");
        }


        //fixer _id pour chercher bon_livraison
        body.set_id(body.getBon_livraison());


        couchDbService.saveCommande(body);


        return ResponseEntity.ok()
                .header("Custom-Header", "Commande enregistrée")
                .body("Colis enregistré pour BL: " + body.getBon_livraison());
    }

}

