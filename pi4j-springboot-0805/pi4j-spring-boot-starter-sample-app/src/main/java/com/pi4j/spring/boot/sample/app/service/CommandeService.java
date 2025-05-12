package com.pi4j.spring.boot.sample.app.service;

import com.pi4j.spring.boot.sample.app.model.Commande;
import com.pi4j.spring.boot.sample.app.repository.CommandeRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service

public class CommandeService {

    private final CommandeRepository commandeRepository;

    public CommandeService(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    /**
     * 保存或更新 Commande
     */
    public Commande saveOrUpdate(Commande commande) {
        System.out.println("Sauvegarde de la commande avec bon_livraison: " + commande.getBon_livraison());
        return commandeRepository.save(commande);
    }


    /**
     * 根据 bon_livraison 查询 Commande
     */
    public Optional<Commande> findByIdColis(String bon_livraison) {
        System.out.println("Recherche de la commande avec bon_livraison: " + bon_livraison);
        return commandeRepository.findByBon_livraison(bon_livraison);
    }

}


