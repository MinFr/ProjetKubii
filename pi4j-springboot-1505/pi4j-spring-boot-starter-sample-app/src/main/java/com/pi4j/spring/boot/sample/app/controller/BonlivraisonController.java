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
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;
import com.pi4j.spring.boot.sample.app.model.BonlivraisonRequest;
import com.pi4j.spring.boot.sample.app.service.CacheService;
import com.pi4j.spring.boot.sample.app.service.Pi4JService;
import com.pi4j.spring.boot.sample.app.component.CagetteAllouer;
import com.pi4j.spring.boot.sample.app.component.ActiveCommande;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class BonlivraisonController{

    private final CacheService cacheService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CagetteAllouer cagetteAllouer;
    private final ActiveCommande activeCommande;
    
    //private List<Bonlivraison> reception_bl = new List<Bonlivraison>();
    
    @Value("${mock.api.url:http://192.168.68.65:3000/api/kubii/commande}")
    private String apiUrl;

    public BonlivraisonController(CacheService cacheService,CagetteAllouer cagetteAllouer,ActiveCommande activeCommande) {
        this.cacheService = cacheService;
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
        apiUrl, HttpMethod.POST, entity, Bonlivraison.class);

        Bonlivraison json_body = response.getBody();

        if (json_body == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: Réponse vide de l'API");
        }

        int numeroCagette = cagetteAllouer.allocateRandom();
        json_body.setNumero_cagette_alloue(numeroCagette);
        //System.out.println("cagettes disponibles " + cagetteAllouer.);
        json_body.setIp_controller("ici c'est ip_controller");

System.out.println("================Json "
+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json_body));

activeCommande.register(json_body,numeroCagette);

System.out.println("====Json enregistre dans le map");

return ResponseEntity.ok("Bonlivraison enregistrée pour BL: " + json_body.getBon_livraison());
}



@GetMapping("/cache/{bl}")
public ResponseEntity<Bonlivraison> getFromCache(@PathVariable String bl) {
    Bonlivraison Bonlivraison = cacheService.get(bl);
    if (Bonlivraison == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(Bonlivraison);
}

}