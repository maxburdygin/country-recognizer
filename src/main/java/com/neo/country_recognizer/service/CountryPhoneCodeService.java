package com.neo.country_recognizer.service;

import com.neo.country_recognizer.model.CountryPhoneCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryPhoneCodeService {

    @Autowired
    private GuavaDataCache cache;

    public CountryPhoneCode findCountryByPhoneNumber(String phoneNumber) {
        List<CountryPhoneCode> countryCodes = null;

        for (int i = 3; i >= 1; i--) {
            String code = phoneNumber.substring(0, i);
            countryCodes = cache.getCountryPhoneCodesFromCache(code);
            if (!countryCodes.isEmpty()) {
                break;
            }
        }

        if (countryCodes.isEmpty()) {
            for (int i = 3; i >= 1; i--) {
                String code = phoneNumber.substring(0, i);
                countryCodes = cache.getCountryPhoneCodes(code);
                if (!countryCodes.isEmpty()) {
                    break;
                }
            }
        }

        if (countryCodes.isEmpty()) {
            return new CountryPhoneCode();
        }

        countryCodes = sortCountryCodes(countryCodes);
        for (CountryPhoneCode countryPhoneCode : countryCodes) {
            String codeCombined = countryPhoneCode.getCode() + countryPhoneCode.getAdditionalCode();
            if (phoneNumber.startsWith(codeCombined)) {
                return countryPhoneCode;
            }
        }

        Optional<CountryPhoneCode> result = countryCodes.stream()
                .filter(countryPhoneCode -> countryPhoneCode.getAdditionalCode() == null || countryPhoneCode.getAdditionalCode().isEmpty())
                .findFirst();

        return result.orElseGet(CountryPhoneCode::new);
    }

    private List<CountryPhoneCode> sortCountryCodes(List<CountryPhoneCode> countryCodes) {
        return countryCodes.stream()
                .sorted(Comparator.comparing(countryPhoneCode ->
                        countryPhoneCode.getAdditionalCode() == null || countryPhoneCode.getAdditionalCode().isEmpty()))
                .collect(Collectors.toList());
    }
}



