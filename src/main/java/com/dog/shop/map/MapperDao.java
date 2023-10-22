package com.dog.shop.map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class MapperDao {
    private final ConcurrentMap<String, String> inMemoryDataStore = new ConcurrentHashMap<String, String>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void setValues(String key, String data) {
        inMemoryDataStore.put(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        inMemoryDataStore.put(key, data);
        executorService.schedule(() -> deleteValues(key), duration.toMillis(), TimeUnit.MICROSECONDS);
    }

    public String getValues(String key) {
        return inMemoryDataStore.get(key);
    }


    public void deleteValues(String key) {
        inMemoryDataStore.remove(key);
    }
}