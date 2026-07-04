package com.qatracker.repository;

import com.qatracker.model.Defect;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
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
