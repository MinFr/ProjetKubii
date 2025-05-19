package com.pi4j.spring.boot.sample.app.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CagetteAllouer {

    private final Set<Integer> usedCagettes = new HashSet<>();
    private final List<Integer> allCagettes = Arrays.asList(1, 2, 3, 4, 5, 6);
    private final Random random = new Random();

    public synchronized Optional<Integer> allocateRandom() {
        List<Integer> freeCagettes = new ArrayList<>(allCagettes);
        freeCagettes.removeAll(usedCagettes);

        if (freeCagettes.isEmpty()) {
            System.out.println("Toutes les cagettes sont occupées. Ignoré sans exception.");
            return Optional.empty();  
        }

        int choice = freeCagettes.get(random.nextInt(freeCagettes.size()));
        usedCagettes.add(choice);
        return Optional.of(choice);
    }

    public synchronized void release(Integer num) {
        usedCagettes.remove(num);
    }

    public synchronized void reset() {
        usedCagettes.clear();
    }

    public synchronized boolean isUsed(Integer num) {
        return usedCagettes.contains(num);
    }

    public synchronized List<Integer> getFreeCagettes() {
        List<Integer> free = new ArrayList<>(allCagettes);
        free.removeAll(usedCagettes);
        return free;
    }
}



