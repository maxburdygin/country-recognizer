package com.neo.country_recognizer.controller;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.service.CountryPhoneCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CountryCodeController.class)
public class CountryCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryPhoneCodeService service;

    private CountryPhoneCode countryPhoneCode;

    @BeforeEach
    public void setUp() {
        countryPhoneCode = new CountryPhoneCode();
        countryPhoneCode.setCountry("United States");
        countryPhoneCode.setCode("1");
    }

    @Test
    public void testGetCountryByPhoneNumberSuccess() throws Exception {
        String phoneNumber = "1234567890";
        when(service.findCountryByPhoneNumber(phoneNumber)).thenReturn(countryPhoneCode);

        mockMvc.perform(post("/api/country")
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("United States"))
                .andExpect(jsonPath("$.code").value("1"));
    }

    @Test
    public void testGetCountryByPhoneNumberNotFound() throws Exception {
        String phoneNumber = "1234567890";
        when(service.findCountryByPhoneNumber(phoneNumber)).thenReturn(null);

        mockMvc.perform(post("/api/country")
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void testGetCountryByPhoneNumberInvalidPhoneNumber() throws Exception {
        String phoneNumber = "invalid_number";
        when(service.findCountryByPhoneNumber(phoneNumber)).thenReturn(new CountryPhoneCode());

        mockMvc.perform(post("/api/country")
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").isEmpty())
                .andExpect(jsonPath("$.code").isEmpty())
                .andExpect(jsonPath("$.additionalCode").isEmpty());
    }

    @Test
    public void testGetCountryByPhoneNumberServiceException() throws Exception {
        String phoneNumber = "1234567890";
        when(service.findCountryByPhoneNumber(phoneNumber)).thenThrow(new RuntimeException("Service exception"));

        mockMvc.perform(post("/api/country")
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testPostMethodAndPath() throws Exception {
        mockMvc.perform(post("/api/country")
                        .param("phoneNumber", "1234567890"))
                .andExpect(status().isOk());
    }
}