package com.pi4j.spring.boot.sample.app.service;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColisRepositoryService extends MongoRepository<ColisService, String> {
    // findByEtatColis 
}

