package com.pi4j.spring.boot.sample.app.service;

import org.springframework.stereotype.Component;
import com.pi4j.spring.boot.sample.app.model.Bonlivraison;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheService{

    private final Map<String, Bonlivraison> cache = new ConcurrentHashMap<>();

    public void put(String id, Bonlivraison bonlivraison) {
        cache.put(id, bonlivraison);
    }

    public Bonlivraison get(String id) {
        return cache.get(id);
    }


    public boolean contains(String id) {
        return cache.containsKey(id);
    }


    public void remove(String id) {
        cache.remove(id);
    }


    public Map<String, Bonlivraison> getAll() {
        return cache;
    }
}
