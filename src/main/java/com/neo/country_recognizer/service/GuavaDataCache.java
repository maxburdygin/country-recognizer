package com.neo.country_recognizer.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.repository.CountryPhoneCodeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

@Service
public class GuavaDataCache {

    @Autowired
    private CountryPhoneCodeRepository repository;

    private final LoadingCache<String, List<CountryPhoneCode>> cache;

    private static final Logger logger = LoggerFactory.getLogger(GuavaDataCache.class);

    public GuavaDataCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .build(new CacheLoader<String, List<CountryPhoneCode>>() {
                    @Override
                    public List<CountryPhoneCode> load(String key) {
                        return new CopyOnWriteArrayList<>();
                    }
                });
    }

    @Transactional
    public void addCountryPhoneCode(CountryPhoneCode countryPhoneCode) {
        try {
            List<CountryPhoneCode> countryPhoneCodes = cache.get(countryPhoneCode.getCode());
            if (!countryPhoneCodes.contains(countryPhoneCode)) {
                countryPhoneCodes.add(countryPhoneCode);
                cache.put(countryPhoneCode.getCode(), countryPhoneCodes);
                logger.debug("{} was added to cache", countryPhoneCode);
                if (repository.findByCodeAndAdditionalCode(countryPhoneCode.getCode(), countryPhoneCode.getAdditionalCode()).isEmpty()) {
                    repository.save(countryPhoneCode);
                    logger.debug("{} was saved to DB", countryPhoneCode);
                }
            }
        } catch (ExecutionException e) {
            logger.error("Error accessing cache", e);
        }
    }

    public List<CountryPhoneCode> getCountryPhoneCodes(String code) {
        try {
            return cache.get(code);
        } catch (ExecutionException e) {
            logger.error("Error accessing cache", e);
            return new ArrayList<>();
        }
    }
}
