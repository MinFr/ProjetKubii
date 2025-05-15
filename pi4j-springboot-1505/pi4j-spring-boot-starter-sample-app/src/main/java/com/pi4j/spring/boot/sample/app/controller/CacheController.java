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
import com.pi4j.spring.boot.sample.app.model.ColisArticle;
import com.pi4j.spring.boot.sample.app.model.Commande;
import com.pi4j.spring.boot.sample.app.model.CommandeRequest;
import com.pi4j.spring.boot.sample.app.model.ScanRequest;
import com.pi4j.spring.boot.sample.app.service.CacheService;
import com.pi4j.spring.boot.sample.app.service.CouchDbService;
import com.pi4j.spring.boot.sample.app.service.Pi4JService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class CacheController{


    private final CouchDbService couchDbService;
    private final CacheService cacheService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Pi4JService pi4JService;


    @Value("${mock.api.url:http://192.168.68.65:3000/api/kubii/commande}")
    private String apiUrl;


    public CacheController(CouchDbService couchDbService, CacheService cacheService,Pi4JService pi4JService) {
        this.couchDbService = couchDbService;
        this.cacheService = cacheService;
        this.pi4JService = pi4JService;
    }


    @PostMapping("/retour_bonlivraison")
    public ResponseEntity<String> postBonLivraison(@RequestBody CommandeRequest request, HttpServletRequest servletRequest) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        System.out.println("HTTP Method: " + servletRequest.getMethod());
        System.out.println("JSON Request: " + mapper.writeValueAsString(request));


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

        body.set_id(body.getBon_livraison());

        cacheService.put(body.get_id(), body);
        System.out.println("Commande mise en cache: " + body.get_id());

        return ResponseEntity.ok("Commande enregistrée en mémoire pour BL: " + body.getBon_livraison());
    }

@PostMapping("/scan")
public ResponseEntity<String> scanColis(@RequestBody ScanRequest request) {
    String ean = request.getEan13();
    String bl = request.getBonLivraison();

    if (!cacheService.contains(bl)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande non trouvée en mémoire");
    }

    Commande commande = cacheService.get(bl);
    boolean updated = false;

    for (ColisArticle article : commande.getCommande_colis_articles()) {
        if (article.getEan13_product().equals(ean)) {

            if (article.getQte_scannee() < article.getQty()) {
            article.setQte_scannee(article.getQte_scannee() + 1);

            pi4JService.blinkColor("purple");

            System.out.println("QTE mise à jour : " + article.getQte_scannee());
            
            if (article.getQte_scannee() >= article.getQty()) {
            article.setEtat_colis("complet");
            System.out.println("État du colis : " + article.getEtat_colis());

        }
    } else {
        System.out.println("❗Quantité déjà atteinte : " + article.getQte_scannee());

        pi4JService.blinkColor("red");
    }
    updated = true;
    break;
}
    }

    if (updated) {
        pi4JService.setColor("green");
        return ResponseEntity.ok("Colis mis à jour en mémoire pour BL: " + bl);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article non trouvé dans la commande");
    }

}

@GetMapping("/cache/{bl}")
public ResponseEntity<Commande> getFromCache(@PathVariable String bl) {
    Commande commande = cacheService.get(bl);
    if (commande == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(commande);
}

}