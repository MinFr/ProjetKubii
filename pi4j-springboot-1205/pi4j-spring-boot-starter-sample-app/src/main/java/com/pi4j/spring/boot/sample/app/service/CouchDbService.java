package com.pi4j.spring.boot.sample.app.service;

import com.pi4j.spring.boot.sample.app.model.Commande;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CouchDbService {

    @Value("${couchdb.host}")
    private String couchDbUrl;

    @Value("${couchdb.db}")
    private String dbName;

    @Value("${couchdb.user}")
    private String username;

    @Value("${couchdb.password}")
    private String password;


    private final RestTemplate restTemplate = new RestTemplate();


    public Commande saveCommande(Commande commande) {
        String url = couchDbUrl + "/" + dbName;


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);


        HttpEntity<Commande> entity = new HttpEntity<>(commande, headers);


        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);


        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println(" Commande enregistrée dans CouchDB");
            return commande;
        } else {
            throw new RuntimeException("Erreur enregistrement CouchDB: " + response.getBody());
        }
    }


    public Optional<Commande> getCommandeById(String id) {
        String url = couchDbUrl + "/" + dbName + "/" + id;


        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);


        HttpEntity<Void> entity = new HttpEntity<>(headers);


        try {
            ResponseEntity<Commande> response = restTemplate.exchange(url, HttpMethod.GET, entity, Commande.class);
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}

