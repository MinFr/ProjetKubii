package com.pi4j.spring.boot.sample.app.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pi4j.spring.boot.sample.app.component.ActiveCommande;
import com.pi4j.spring.boot.sample.app.component.CagetteAllouer;
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;
import com.pi4j.spring.boot.sample.app.model.ColisArticle;
import com.pi4j.spring.boot.sample.app.model.ScanRequest;
import com.pi4j.spring.boot.sample.app.service.CouchDbService;
import com.pi4j.spring.boot.sample.app.service.Pi4JService;


import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ScanController {


    private final ActiveCommande activeCommande;
    private final Pi4JService pi4JService;
    private final CagetteAllouer cagetteAllouer;
    private final CouchDbService couchDbService;


    public ScanController(ActiveCommande activeCommande, Pi4JService pi4JService,CagetteAllouer cagetteAllouer,CouchDbService couchDbService) {
        this.activeCommande = activeCommande;
        this.pi4JService = pi4JService;
        this.cagetteAllouer = cagetteAllouer;
        this.couchDbService = couchDbService;
    }


    @PostMapping("/scan")
    public ResponseEntity<String> scanColis(@RequestBody ScanRequest request) {
        String ean = request.getEan13();
        String bl = request.getBonLivraison();

        Optional<Map.Entry<Integer, Bonlivraison>> entryOpt = activeCommande.getAll().entrySet().stream()
                .filter(entry -> entry.getValue().getBon_livraison().equals(bl))
                .findFirst();


        if (entryOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("Bon de livraison introuvable dans les cagettes actives");
        }


        Map.Entry<Integer, Bonlivraison> entry = entryOpt.get();
        int cagetteNum = entry.getKey();
        Bonlivraison bon = entry.getValue();

        boolean updated = false;

        for (ColisArticle article : bon.getCommande_colis_articles()) {
            if (article.getEan13_product().equals(ean)) {
                if (article.getQte_scannee() < article.getQty()) {
                    article.setQte_scannee(article.getQte_scannee() + 1);
                    pi4JService.blinkColor("purple");
                    System.out.println(" [Scan] BL " + bl + " | Article " + ean +
                            " | Qte scannée: " + article.getQte_scannee());


                    if (article.getQte_scannee() >= article.getQty()) {
                        article.setEtat_colis("complet");
                        System.out.println(" Article complet !");
                    }
                } else {
                    System.out.println(" Article déjà complet (qte=" + article.getQte_scannee() + ")");
                    pi4JService.blinkColor("red");
                }
                updated = true;
                break;
            }
        }


        // Affichage de tous les BL actifs
        System.out.println("\n===== État des cagettes actives =====");
        for (Map.Entry<Integer, Bonlivraison> activeEntry : activeCommande.getAll().entrySet()) {
            int num = activeEntry.getKey();
            Bonlivraison b = activeEntry.getValue();


            System.out.println("🔲 Cagette " + num + " > BL: " + b.getBon_livraison());
            for (ColisArticle a : b.getCommande_colis_articles()) {
                System.out.println("  - Produit: " + a.getEan13_product() +
                        " | Qte: " + a.getQte_scannee() + "/" + a.getQty() +
                        " | État: " + a.getEtat_colis());
            }
        }


        boolean complet = bon.getCommande_colis_articles().stream()
                .allMatch(a -> "complet".equalsIgnoreCase(a.getEtat_colis()));



        if (complet) {
            System.out.println(" BL " + bl + " terminé. Libération de la cagette " + cagetteNum);
            couchDbService.saveBonlivraison(bon);
            activeCommande.release(cagetteNum);
            cagetteAllouer.release(cagetteNum);
            pi4JService.setColor("off");
        }


        if (updated) {
            //pi4JService.setColor("green");
            return ResponseEntity.ok("Colis mis à jour pour BL: " + bl);
        } else {
            return ResponseEntity.status(404)
                    .body(" Article non trouvé dans la Bonlivraison");
        }
    }
}


