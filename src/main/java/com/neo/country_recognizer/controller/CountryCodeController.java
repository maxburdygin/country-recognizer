package com.neo.country_recognizer.controller;


import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.service.CountryPhoneCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CountryCodeController {

    @Autowired
    private CountryPhoneCodeService service;

    @PostMapping("/country")
    public ResponseEntity<CountryPhoneCode> getCountryByPhoneNumber(@RequestParam String phoneNumber) {
        try {
            CountryPhoneCode countryPhoneCode = service.findCountryByPhoneNumber(phoneNumber);
            return ResponseEntity.ok(countryPhoneCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
