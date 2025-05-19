package com.pi4j.spring.boot.sample.app.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;

@Component
public class ActiveCommande {

    // key = cagette number, value = bon de livraison
    private final Map<Integer, Bonlivraison> activeCommandes = new HashMap<>();
    private final int MAX_CAPACITY = 6;

    public synchronized boolean isFull() {
        return activeCommandes.size() >= MAX_CAPACITY;
    }


    public synchronized boolean containsBon(String bonLivraison) {
        return activeCommandes.values().stream()
                .anyMatch(bl -> bl.getBon_livraison().equals(bonLivraison));
    }


    public synchronized Optional<Integer> register(Bonlivraison bon, int cagetteNum) {
    
        if (activeCommandes.size() >= 6) {
            System.out.println(" Limite atteinte : 6 commandes actives. Ignoré sans erreur.");
            return Optional.empty();
        }
    
        activeCommandes.put(cagetteNum, bon);
        System.out.println(" Nouveau BL " + bon.getBon_livraison() + " ajouté à la cagette " + cagetteNum);
        return Optional.of(cagetteNum);
    }
    

    public synchronized void release(int cagetteNum) {
        activeCommandes.remove(cagetteNum);
    }

    public synchronized Map<Integer, Bonlivraison> getAll() {
        return Collections.unmodifiableMap(activeCommandes);
    }


    public synchronized Optional<Integer> getCagetteByBon(String bonLivraison) {
        return activeCommandes.entrySet().stream()
                .filter(e -> e.getValue().getBon_livraison().equals(bonLivraison))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
