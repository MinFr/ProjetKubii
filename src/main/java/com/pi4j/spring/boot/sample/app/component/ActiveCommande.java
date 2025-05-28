package com.pi4j.spring.boot.sample.app.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;

@Component
public class ActiveCommande {

    // key = cagette number, value = bon de livraison
    private final Map<Integer, Bonlivraison> activeCommandes = new HashMap<>();
    private final int MAX_CAPACITY = 6;

    // Vérifie si le nombre de commandes actives a atteint la limite
    public synchronized boolean isFull() {
        return activeCommandes.size() >= MAX_CAPACITY;
    }

    // Vérifie si un bon de livraison est déjà enregistré
    public synchronized boolean containsBon(String bonLivraison) {
        return activeCommandes.values().stream()
                .anyMatch(bl -> bl.getBon_livraison().equals(bonLivraison));
    }

    // Enregistre une nouvelle commande dans une cagette spécifique
    public synchronized Optional<Integer> register(Bonlivraison bon, int cagetteNum) {
        if (activeCommandes.size() >= MAX_CAPACITY) {
            System.out.println("❗ Limite atteinte : 6 commandes actives. Ignoré.");
            return Optional.empty();
        }

        activeCommandes.put(cagetteNum, bon);
        System.out.println("✅ Nouveau BL " + bon.getBon_livraison() + " ajouté à la cagette " + cagetteNum);
        return Optional.of(cagetteNum);
    }

    // Libère une cagette
    public synchronized void release(int cagetteNum) {
        activeCommandes.remove(cagetteNum);
    }

    // Retourne toutes les commandes actives (non modifiable)
    public synchronized Map<Integer, Bonlivraison> getAll() {
        return Collections.unmodifiableMap(activeCommandes);
    }

    // Trouve le numéro de cagette par BL
    public synchronized Optional<Integer> getCagetteByBon(String bonLivraison) {
        return activeCommandes.entrySet().stream()
                .filter(e -> e.getValue().getBon_livraison().equals(bonLivraison))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    //  (Nouveau) Retourne les BL activés par opérateur (i.e., prêts pour scan article)
    public synchronized List<Bonlivraison> getScannableBonlivraisons() {
        List<Bonlivraison> list = new ArrayList<>();
        for (Bonlivraison bl : activeCommandes.values()) {
            if (bl.getId_operateur() != null && !bl.getId_operateur().isEmpty()) {
                list.add(bl);
            }
        }
        return list;
    }
}

