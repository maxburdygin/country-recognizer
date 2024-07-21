package com.neo.country_recognizer.integration;

import com.neo.country_recognizer.service.GuavaDataCache;
import com.neo.country_recognizer.service.WikipediaDataLoader;
import com.neo.country_recognizer.util.JsoupClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class WikipediaDataLoaderIntegrationTest {

    @Mock
    private GuavaDataCache cache;

    @Mock
    private JsoupClient jsoupClient;

    @InjectMocks
    private WikipediaDataLoader wikipediaDataLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateCountryCodesIntegration() throws IOException {
        // Load HTML from file
        String html = loadHtmlFromFile("/wiki-upload/test_wikipedia_page.html");
        Document mockDocument = Jsoup.parse(html);

        // Prepare mock data
        when(jsoupClient.getDocument(anyString())).thenReturn(mockDocument);

        // Run the method
        wikipediaDataLoader.updateCountryCodes();

        // Verify interactions with the cache
        verify(cache).addCountryPhoneCode(argThat(code ->
                "Country1".equals(code.getCountry()) &&
                        "1".equals(code.getCode()) &&
                        code.getAdditionalCode() == null
        ));

        verify(cache).addCountryPhoneCode(argThat(code ->
                "Country1".equals(code.getCountry()) &&
                        "1".equals(code.getCode()) &&
                        "2".equals(code.getAdditionalCode())
        ));

        verify(cache).addCountryPhoneCode(argThat(code ->
                "Country1".equals(code.getCountry()) &&
                        "1".equals(code.getCode()) &&
                        "3".equals(code.getAdditionalCode())
        ));

        verify(cache).addCountryPhoneCode(argThat(code ->
                "Country2".equals(code.getCountry()) &&
                        "4".equals(code.getCode()) &&
                        code.getAdditionalCode() == null
        ));
    }

    private String loadHtmlFromFile(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new String(is.readAllBytes());
        }
    }
}
