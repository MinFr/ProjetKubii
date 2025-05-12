package com.pi4j.spring.boot.sample.app.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pi4j.spring.boot.sample.app.model.Commande;

public interface CommandeRepository extends MongoRepository<Commande, String> {
    // 基于 bon_livraison 查找 Commande
    @Query("{ 'bon_livraison': ?0 }")
    Optional<Commande> findByBon_livraison(String bon_livraison);
}
