package com.neo.country_recognizer.controller;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.service.CountryPhoneCodeService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CountryCodeController {

    @Autowired
    private CountryPhoneCodeService service;

    @PostMapping("/country")
    public ResponseEntity<List<CountryPhoneCode>> getCountryByPhoneNumber(@RequestBody PhoneNumberRequest request) {
        String phoneNumber = request.getPhoneNumber();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            List<CountryPhoneCode> countryPhoneCodes = service.findCountryByPhoneNumber(phoneNumber);
            return ResponseEntity.ok(countryPhoneCodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Data
    public static class PhoneNumberRequest {
        private String phoneNumber;
    }
}
