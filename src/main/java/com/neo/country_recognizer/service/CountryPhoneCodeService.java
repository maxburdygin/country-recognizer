package com.neo.country_recognizer.service;

import com.neo.country_recognizer.model.CountryPhoneCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CountryPhoneCodeService {

    @Autowired
    private GuavaDataCache cache;

    public List<CountryPhoneCode> findCountryByPhoneNumber(String phoneNumber) {
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
            return Collections.emptyList();
        }
        countryCodes = sortCountryCodes(countryCodes);
        for (CountryPhoneCode countryPhoneCode : countryCodes) {
            String codeCombined = countryPhoneCode.getCode() + countryPhoneCode.getAdditionalCode();
            if (phoneNumber.startsWith(codeCombined)) {
                return countryCodes.stream()
                        .filter(x -> (Objects.equals(x.getCode(), countryPhoneCode.getCode()) &&
                                Objects.equals(x.getAdditionalCode(), countryPhoneCode.getAdditionalCode())))
                        .toList();
            }
        }

        return countryCodes.stream()
                .filter(countryPhoneCode -> countryPhoneCode.getAdditionalCode() == null || countryPhoneCode.getAdditionalCode().isEmpty())
                .toList();
    }

    private List<CountryPhoneCode> sortCountryCodes(List<CountryPhoneCode> countryCodes) {
        return countryCodes.stream()
                .sorted(Comparator.comparing(countryPhoneCode ->
                        countryPhoneCode.getAdditionalCode() == null || countryPhoneCode.getAdditionalCode().isEmpty()))
                .collect(Collectors.toList());
    }
}



