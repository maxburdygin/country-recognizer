package com.neo.country_recognizer.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.neo.country_recognizer.model.CountryPhoneCode;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GuavaDataCache {
    private static final LoadingCache<String, List<CountryPhoneCode>> cache = CacheBuilder.newBuilder()
            .maximumSize(500) // Максимальный размер кеша
            .build(new CacheLoader<String, List<CountryPhoneCode>>() {
                @Override
                public List<CountryPhoneCode> load(String key) throws Exception {
                    return loadDataFromDatabase(key);
                }
            });

    private static List<CountryPhoneCode> loadDataFromDatabase(String key) {
        // Логика загрузки данных из базы данных
        return Collections.emptyList();
    }

    public static List<CountryPhoneCode> get(String key) throws ExecutionException {
        return cache.get(key);
    }
}
