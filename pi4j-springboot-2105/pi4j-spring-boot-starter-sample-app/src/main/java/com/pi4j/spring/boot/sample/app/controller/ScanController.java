package com.pi4j.spring.boot.sample.app.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.pi4j.spring.boot.sample.app.component.ActiveCommande;
import com.pi4j.spring.boot.sample.app.component.CagetteAllouer;
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;
import com.pi4j.spring.boot.sample.app.model.BonlivraisonRequest;
import com.pi4j.spring.boot.sample.app.model.ColisArticle;
import com.pi4j.spring.boot.sample.app.model.ScanRequest;
import com.pi4j.spring.boot.sample.app.service.CouchDbService;
import com.pi4j.spring.boot.sample.app.service.Pi4JService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ScanController {

    private final ActiveCommande activeCommande;
    private final Pi4JService pi4JService;
    private final CagetteAllouer cagetteAllouer;
    private final CouchDbService couchDbService;
    private final RestTemplate restTemplate = new RestTemplate();
    private boolean operateurModeActivated = false;

    public ScanController(ActiveCommande activeCommande, Pi4JService pi4JService, CagetteAllouer cagetteAllouer, CouchDbService couchDbService) {
        this.activeCommande = activeCommande;
        this.pi4JService = pi4JService;
        this.cagetteAllouer = cagetteAllouer;
        this.couchDbService = couchDbService;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> scanColis(@RequestBody ScanRequest request) {
        String code = request.getEan13().trim();
        String bonLivraisonFromClient = request.getBonLivraison();
        boolean updated = false;

        System.out.println("[Scan reçu] code=" + code);

        // 1. opérateur
        if (code.toLowerCase().contains("operateur") && !operateurModeActivated) {
            for (Bonlivraison bl : activeCommande.getAll().values()) {
                if (bl.getId_operateur() == null) {
                    bl.setId_operateur(code);
                    System.out.println("🧍 Opérateur assigné à BL " + bl.getBon_livraison() + " : " + code);
                }
            }
            operateurModeActivated = true;
            return ResponseEntity.ok(" Mode opérateur activé. BL verrouillés. Vous pouvez scanner les articles.");
        }

        // 2. contrôleur (ne peut être scanné que si TOUS les BL sont complets)
        if (code.toLowerCase().contains("controleur")) {
            boolean tousComplets = activeCommande.getAll().values().stream()
                .allMatch(bl -> bl.getCommande_colis_articles().stream()
                    .allMatch(a -> "complet".equalsIgnoreCase(a.getEtat_colis())));

            if (!tousComplets) {
                return ResponseEntity.status(400).body("Tous les articles ne sont pas encore complets. Attendez.");
            }

            for (Map.Entry<Integer, Bonlivraison> entry : activeCommande.getAll().entrySet()) {
                Bonlivraison bl = entry.getValue();
                if (bl.getId_controller() == null) {
                    bl.setId_controller(code);
                    bl.setDate_validation_cagette_termine(LocalDateTime.now().toString());
                    couchDbService.saveBonlivraison(bl);
                    activeCommande.release(entry.getKey());
                    cagetteAllouer.release(entry.getKey());
                    System.out.println(" Contrôleur validé pour BL " + bl.getBon_livraison());
                }
            }

            pi4JService.setColor("off");
            return ResponseEntity.ok(" Tous les BL validés avec le contrôleur " + code);
        }

        // 3. Si opérateur actif, scanner article globalement
        if (operateurModeActivated) {
            for (Map.Entry<Integer, Bonlivraison> entry : activeCommande.getAll().entrySet()) {
                Bonlivraison bl = entry.getValue();

                if (bl.getId_operateur() == null) continue; // skip si pas encore activé

                for (ColisArticle article : bl.getCommande_colis_articles()) {
                    if (article.getEan13_product().equals(code)) {
                        if (article.getQte_scannee() < article.getQty()) {
                            article.setQte_scannee(article.getQte_scannee() + 1);
                            pi4JService.blinkColor("purple");
                            System.out.println("🔄 Article scanné pour BL " + bl.getBon_livraison());

                            if (article.getQte_scannee() >= article.getQty()) {
                                article.setEtat_colis("complet");
                                System.out.println(" Article complet pour " + article.getEan13_product());
                            }
                        } else {
                            pi4JService.blinkColor("red");
                            System.out.println(" Article déjà complet");
                        }
                        updated = true;
                        break;
                    }
                }
                if (updated) break; // si article trouvé, pas besoin de continuer
            }

            if (updated) {
                return ResponseEntity.ok(" Article scanné et mis à jour.");
            } else {
                pi4JService.blinkColor("red");
                return ResponseEntity.status(404).body(" Article non trouvé dans les BL actifs.");
            }
        }

        // 4. Interdiction d'ajouter BL après opérateur
        if (operateurModeActivated && code.matches("^\\d{10,}$")) {
            return ResponseEntity.status(403).body("❌ Impossible d'ajouter un nouveau BL après l'activation opérateur.");
        }

        // 5. Ajout BL (avant opérateur uniquement)
        if (!operateurModeActivated && code.matches("^\\d{10,}$")) {
            BonlivraisonRequest requestBody = new BonlivraisonRequest();
            requestBody.setBon_livraison(code);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BonlivraisonRequest> entity = new HttpEntity<>(requestBody, headers);
            try {
                ResponseEntity<String> result = restTemplate.postForEntity(
                        "http://localhost:8080/api/retour_bonlivraison", entity, String.class);
                return result;
            } catch (Exception e) {
                return ResponseEntity.status(500).body("❌ Erreur appel retour_bonlivraison : " + e.getMessage());
            }
        }

        return ResponseEntity.status(400).body("❌ Code non reconnu ou contexte invalide.");
    }
}

