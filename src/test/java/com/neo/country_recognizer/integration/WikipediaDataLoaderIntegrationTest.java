package com.neo.country_recognizer.integration;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.service.GuavaDataCache;
import com.neo.country_recognizer.service.WikipediaDataLoader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class WikipediaDataLoaderIntegrationTest {

    @Autowired
    private WikipediaDataLoader wikipediaDataLoader;

    @Autowired
    private GuavaDataCache cache;

    @BeforeEach
    @Transactional // Ensure a clean state for each test
    public void setUp() throws IOException {
        cache.clear();
    }

    @Test
    public void testUpdateCountryCodesIntegration() throws IOException {
        // Load HTML from file
        String html = new String(Files.readAllBytes(Paths.get("src/test/resources/wiki-upload/test_wikipedia_page.html")));
        Document mockDocument = Jsoup.parse(html);

        wikipediaDataLoader.updateCountryCodes();

        // Verify the results in the cache
        List<CountryPhoneCode> cachedData = cache.getCountryPhoneCodes("7");
        assertEquals(8, cachedData.size());

        cachedData = cache.getCountryPhoneCodes("299");
        assertEquals(1, cachedData.size());
    }
}

