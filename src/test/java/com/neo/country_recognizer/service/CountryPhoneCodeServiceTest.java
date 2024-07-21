package com.neo.country_recognizer.service;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.repository.CountryPhoneCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CountryPhoneCodeServiceTest {

    @InjectMocks
    private CountryPhoneCodeService service;

    @Mock
    private GuavaDataCache cache;

    @Mock
    private CountryPhoneCodeRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindCountryByPhoneNumberInCache() {
        String phoneNumber = "79146486117";
        String code = "7";

        CountryPhoneCode countryPhoneCode = new CountryPhoneCode();
        countryPhoneCode.setCode(code);
        countryPhoneCode.setCountry("Russia");

        when(cache.getCountryPhoneCodesFromCache(code)).thenReturn(List.of(countryPhoneCode));

        CountryPhoneCode result = service.findCountryByPhoneNumber(phoneNumber);

        assertEquals("Russia", result.getCountry());
        verify(cache, times(1)).getCountryPhoneCodesFromCache(code);
        verify(cache, never()).getCountryPhoneCodes(code);
    }

    @Test
    public void testFindCountryByPhoneNumberInDb() {
        String phoneNumber = "79146486117";
        String code = "7";

        CountryPhoneCode countryPhoneCode = new CountryPhoneCode();
        countryPhoneCode.setCode(code);
        countryPhoneCode.setCountry("Russia");

        when(cache.getCountryPhoneCodesFromCache(code)).thenReturn(Collections.emptyList());
        when(cache.getCountryPhoneCodes(code)).thenReturn(List.of(countryPhoneCode));

        CountryPhoneCode result = service.findCountryByPhoneNumber(phoneNumber);

        assertEquals("Russia", result.getCountry());
        verify(cache, times(1)).getCountryPhoneCodesFromCache(code);
        verify(cache, times(1)).getCountryPhoneCodes(code);
    }

    @Test
    public void testFindCountryByPhoneNumberNotFound() {
        String phoneNumber = "12345";

        when(cache.getCountryPhoneCodesFromCache(anyString())).thenReturn(Collections.emptyList());
        when(cache.getCountryPhoneCodes(anyString())).thenReturn(Collections.emptyList());

        CountryPhoneCode result = service.findCountryByPhoneNumber(phoneNumber);

        assertEquals(new CountryPhoneCode(), result);
    }

    @Test
    public void testFindCountryByPhoneNumberWithAdditionalCode() {
        String phoneNumber = "1234567890";
        String code = "123";
        String additionalCode = "45";

        CountryPhoneCode countryPhoneCode = new CountryPhoneCode();
        countryPhoneCode.setCode(code);
        countryPhoneCode.setAdditionalCode(additionalCode);
        countryPhoneCode.setCountry("TestCountry");

        when(cache.getCountryPhoneCodesFromCache(code)).thenReturn(List.of(countryPhoneCode));

        CountryPhoneCode result = service.findCountryByPhoneNumber(phoneNumber);

        assertEquals("TestCountry", result.getCountry());
        verify(cache, times(1)).getCountryPhoneCodesFromCache(code);
        verify(cache, never()).getCountryPhoneCodes(code);
    }

    @Test
    public void testFindCountryByPhoneNumberWithEmptyAdditionalCode() {
        String phoneNumber = "1234567890";
        String code = "123";

        CountryPhoneCode countryPhoneCode = new CountryPhoneCode();
        countryPhoneCode.setCode(code);
        countryPhoneCode.setCountry("TestCountry");

        when(cache.getCountryPhoneCodesFromCache(code)).thenReturn(List.of(countryPhoneCode));

        CountryPhoneCode result = service.findCountryByPhoneNumber(phoneNumber);

        assertEquals("TestCountry", result.getCountry());
        verify(cache, times(1)).getCountryPhoneCodesFromCache(code);
        verify(cache, never()).getCountryPhoneCodes(code);
    }
}