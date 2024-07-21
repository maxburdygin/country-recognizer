package com.neo.country_recognizer.service;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.repository.CountryPhoneCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryPhoneCodeService {
    @Autowired
    private CountryPhoneCodeRepository repository;

    public CountryPhoneCode findCountryByPhoneNumber(String phoneNumber) {
        for (int i = 3; i >= 0; i--) {
            String code = phoneNumber.substring(0, i);
            List<CountryPhoneCode> countryCodes = repository.findByCode(code);
            countryCodes = sortCountryCodes(countryCodes);
            if (!countryCodes.isEmpty()) {
                for (CountryPhoneCode countryPhoneCode : countryCodes) {
                    String codeCombined = countryPhoneCode.getCode() + countryPhoneCode.getAdditionalCode();
                    if (phoneNumber.startsWith(codeCombined)) {
                        return countryPhoneCode;
                    }
                }
                Optional<CountryPhoneCode> result = countryCodes.stream()
                        .filter(countryPhoneCode -> countryPhoneCode.getAdditionalCode() == null || countryPhoneCode.getAdditionalCode().isEmpty())
                        .findFirst();
                if (result.isPresent()) return result.get();
            }
        }
        return new CountryPhoneCode();
    }

    // Пример метода, где используется сортировка
    private List<CountryPhoneCode> sortCountryCodes(List<CountryPhoneCode> countryCodes) {
        return countryCodes.stream()
                .sorted(Comparator.comparing(countryPhoneCode ->
                        countryPhoneCode.getAdditionalCode() == null || countryPhoneCode.getAdditionalCode().isEmpty()))
                .collect(Collectors.toList());
    }
}



