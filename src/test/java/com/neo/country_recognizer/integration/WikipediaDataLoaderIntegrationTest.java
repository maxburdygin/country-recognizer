package com.neo.country_recognizer.integration;

import com.neo.country_recognizer.service.GuavaDataCache;
import com.neo.country_recognizer.service.WikipediaDataLoader;
import com.neo.country_recognizer.util.JsoupClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class WikipediaDataLoaderIntegrationTest {

    @Mock
    private GuavaDataCache cache;

    @Mock
    private JsoupClient jsoupClient;

    @InjectMocks
    private WikipediaDataLoader wikipediaDataLoader;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateCountryCodesIntegration() throws IOException {
        // Load HTML from file
        String html = loadHtmlFromFile("/wiki-upload/wikipedia_10 countries.html");
        Document mockDocument = Jsoup.parse(html);
        // Prepare mock data
        when(jsoupClient.getDocument(anyString())).thenReturn(mockDocument);

        // Run the method
        wikipediaDataLoader.updateCountryCodes();

        // Verify interactions with the cache
        verify(cache).addCountryPhoneCode(argThat(code ->
                "Abkhazia".equals(code.getCountry()) &&
                        "7".equals(code.getCode()) &&
                        "840".equals(code.getAdditionalCode())
        ));

        verify(cache).addCountryPhoneCode(argThat(code ->
                "Abkhazia".equals(code.getCountry()) &&
                        "7".equals(code.getCode()) &&
                        "940".equals(code.getAdditionalCode())
        ));

        // Verify that false data was not added to the cache or database
        verify(cache, never()).addCountryPhoneCode(argThat(code ->
                "Argentina".equals(code.getCountry()) &&
                        "54".equals(code.getCode())
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
