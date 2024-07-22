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

import java.util.List;

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
        when(service.findCountryByPhoneNumber(phoneNumber)).thenReturn(List.of(countryPhoneCode));

        mockMvc.perform(post("/api/country")
                        .contentType("application/json")
                        .content("{\"phoneNumber\": \"" + phoneNumber + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("United States"))
                .andExpect(jsonPath("$[0].code").value("1"));
    }

    @Test
    public void testGetCountryByPhoneNumberNotFound() throws Exception {
        String phoneNumber = "1234567890";
        when(service.findCountryByPhoneNumber(phoneNumber)).thenReturn(List.of());

        mockMvc.perform(post("/api/country")
                        .contentType("application/json")
                        .content("{\"phoneNumber\": \"" + phoneNumber + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetCountryByPhoneNumberInvalidPhoneNumber() throws Exception {
        String phoneNumber = "invalid_number";
        when(service.findCountryByPhoneNumber(phoneNumber)).thenReturn(List.of(new CountryPhoneCode()));

        mockMvc.perform(post("/api/country")
                        .contentType("application/json")
                        .content("{\"phoneNumber\": \"" + phoneNumber + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").isEmpty())
                .andExpect(jsonPath("$[0].code").isEmpty())
                .andExpect(jsonPath("$[0].additionalCode").isEmpty());
    }

    @Test
    public void testGetCountryByPhoneNumberServiceException() throws Exception {
        String phoneNumber = "1234567890";
        when(service.findCountryByPhoneNumber(phoneNumber)).thenThrow(new RuntimeException("Service exception"));

        mockMvc.perform(post("/api/country")
                        .contentType("application/json")
                        .content("{\"phoneNumber\": \"" + phoneNumber + "\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testPostMethodAndPath() throws Exception {
        mockMvc.perform(post("/api/country")
                        .contentType("application/json")
                        .content("{\"phoneNumber\": \"1234567890\"}"))
                .andExpect(status().isOk());
    }
}
