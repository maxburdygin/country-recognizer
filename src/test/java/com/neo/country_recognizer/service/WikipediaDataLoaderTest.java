package com.neo.country_recognizer.service;

import com.neo.country_recognizer.TestUtils;
import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.repository.CountryPhoneCodeRepository;
import com.neo.country_recognizer.util.JsoupClient;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WikipediaDataLoaderTest {

    @Mock
    private CountryPhoneCodeRepository repository;

    @Mock
    private JsoupClient jsoupClient;

    @InjectMocks
    private WikipediaDataLoader dataLoader;

    @BeforeEach
    public void setUp() throws IOException {
        // Setup the mock JsoupClient to return a Document from local HTML file
        Document mockDocument = TestUtils.loadDocumentFromResource("/test_wikipedia_page.html");
        when(jsoupClient.getDocument(anyString())).thenReturn(mockDocument);
    }

    @Test
    public void testLoadCountryCodes() throws IOException {
        doNothing().when(repository).deleteAll();
        when(repository.save(any(CountryPhoneCode.class))).thenReturn(null);

        dataLoader.loadCountryCodes();

        verify(repository, times(1)).deleteAll();
        verify(repository, times(295)).save(any(CountryPhoneCode.class));
    }

}

