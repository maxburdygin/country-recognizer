package com.neo.country_recognizer.controller;


import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.service.CountryPhoneCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CountryCodeController {

    @Autowired
    private CountryPhoneCodeService service;

    @PostMapping("/country")
    public CountryPhoneCode getCountryByPhoneNumber(@RequestParam String phoneNumber) {
        return service.findCountryByPhoneNumber(phoneNumber);
    }
}
