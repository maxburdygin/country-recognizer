package com.neo.country_recognizer.service;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.util.JsoupClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WikipediaDataLoaderTest {

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
    public void testUpdateCountryCodes() throws IOException {
        // Mock the Jsoup document and elements
        Document doc = mock(Document.class);
        Element table = mock(Element.class);
        Elements rows = mock(Elements.class);
        Element row = mock(Element.class);
        Elements cols = mock(Elements.class);

        when(jsoupClient.getDocument(anyString())).thenReturn(doc);
        when(doc.select("table.wikitable")).thenReturn(new Elements(table));
        when(table.select("tr")).thenReturn(rows);
        when(rows.get(0)).thenReturn(row);
        when(row.select("td")).thenReturn(cols);
        when(cols.size()).thenReturn(2);
        when(cols.get(0).text()).thenReturn("Russia");
        when(cols.get(1).text()).thenReturn("7 791 795");

        // Setup the test
        when(cols.get(0).text()).thenReturn("Russia");
        when(cols.get(1).text()).thenReturn("7 791 795");

        // Call the method
        wikipediaDataLoader.updateCountryCodes();

        // Verify interactions with cache
        verify(cache, times(1)).addCountryPhoneCode(argThat(code -> code.getCountry().equals("Russia") && code.getCode().equals("7")));
        verify(cache, times(1)).addCountryPhoneCode(argThat(code -> code.getCountry().equals("Russia") && code.getCode().equals("7") && code.getAdditionalCode().equals("791")));
        verify(cache, times(1)).addCountryPhoneCode(argThat(code -> code.getCountry().equals("Russia") && code.getCode().equals("7") && code.getAdditionalCode().equals("795")));
    }

    @Test
    public void testLoadCountryCodes() throws IOException {
        // Setup mocks and call method
        wikipediaDataLoader.loadCountryCodes();

        // Verify interactions
        verify(jsoupClient, times(1)).getDocument(anyString());
        verify(cache, atLeastOnce()).addCountryPhoneCode(any(CountryPhoneCode.class));
    }
}
