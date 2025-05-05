package com.pi4j.spring.boot.sample.app.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;



@Service
public class KubiiApiService {

    private final RestTemplate restTemplate;
    private final ColisRepositoryService colisRepository;

    public KubiiApiService(RestTemplate restTemplate, ColisRepositoryService colisRepository) {
        this.restTemplate = restTemplate;
        this.colisRepository = colisRepository;
    }

    public ColisService envoyerColis(String idColis) {
        String url = "http://localhost:8080/mock-api/colis";

        Map<String, Object> request = Map.of(
                "id_colis", idColis,
                "date-request", "29-04-2025",
                "private_key", "XYZ"
        );


        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);


        ColisService colis = new ColisService();
        colis.setIdColis((String) response.get("id_colis"));
        colis.setClient((String) response.get("client"));
        colis.setEtatColis((String) response.get("etat_colis"));
        colis.setDateCreation((String) response.get("date_creation"));
        colis.setIdProduct((String) response.get("id_product"));
        colis.setEan13Product((String) response.get("ean13_product"));
        colis.setNameProduct((String) response.get("name_product"));
        colis.setQty(Integer.parseInt((String) response.get("qty")));
        colis.setQteScannee(Integer.parseInt((String) response.get("qte_scannee")));
        colis.setEmpCode((String) response.get("Emp_code"));

        colisRepository.save(colis);

        return colis;
    }
}




