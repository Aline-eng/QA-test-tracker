package com.qatracker.repository;

import com.qatracker.model.TestCase;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TestCaseRepository {

    private final Map<Long, TestCase> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public TestCase save(TestCase testCase) {
        if (testCase.getId() == null) {
            testCase.setId(idGenerator.getAndIncrement());
        }
        store.put(testCase.getId(), testCase);
        return testCase;
    }

    public Collection<TestCase> findAll() {
        return store.values();
    }

    public Optional<TestCase> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}
