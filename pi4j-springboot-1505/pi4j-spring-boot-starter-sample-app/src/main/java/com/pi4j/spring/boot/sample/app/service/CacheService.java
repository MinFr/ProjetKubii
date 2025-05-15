package com.pi4j.spring.boot.sample.app.service;

import org.springframework.stereotype.Component;
import com.pi4j.spring.boot.sample.app.model.Commande;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheService{

    private final Map<String, Commande> cache = new ConcurrentHashMap<>();

    public void put(String id, Commande commande) {
        cache.put(id, commande);
    }

    public Commande get(String id) {
        return cache.get(id);
    }


    public boolean contains(String id) {
        return cache.containsKey(id);
    }


    public void remove(String id) {
        cache.remove(id);
    }


    public Map<String, Commande> getAll() {
        return cache;
    }
}
