package com.qatracker.repository;

import com.qatracker.model.Defect;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DefectRepository {
    private final Map<Long, Defect> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Defect save(Defect defect) {
        if (defect.getId() == null) {
            defect.setId(idGenerator.getAndIncrement());
        }
        store.put(defect.getId(), defect);
        return defect;
    }

    public Collection<Defect> findAll() {
        return store.values();
    }
}
