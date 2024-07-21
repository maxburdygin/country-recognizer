package com.neo.country_recognizer.service;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.repository.CountryPhoneCodeRepository;
import com.neo.country_recognizer.util.JsoupClient;
import jakarta.annotation.PostConstruct;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class WikipediaDataLoader {

    private static final String WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes#Alphabetical_order";

    @Autowired
    private CountryPhoneCodeRepository repository;

    @Autowired
    private JsoupClient jsoupClient;

    @PostConstruct
    public void loadCountryCodes() throws IOException {
        updateCountryCodes();
    }

    @Scheduled(cron = "0 * * * * ?") // Выполняется каждую минуту
    public void updateCountryCodes() throws IOException {
        int count = 0;

        Document doc = jsoupClient.getDocument(WIKIPEDIA_URL);
        Element table = doc.select("table.wikitable").first();
        Elements rows = table.select("tr");

        repository.deleteAll(); // TODO - удалить

        for (Element row : rows) {
            count++;
            Elements cols = row.select("td");
            if (cols.size() > 1) {
                String country = cols.get(0).text();
                String codes = cols.get(1).text();

                // Извлечение основного кода и дополнительных кодов
                String[] codeParts = codes.split("\\D+");
                String mainCode = codeParts[0];
                String[] additionalCodes = Arrays.copyOfRange(codeParts, 1, codeParts.length);


                if (additionalCodes.length == 0) {
                    // Сохранение основного кода
                    CountryPhoneCode countryPhoneCode = new CountryPhoneCode();
                    countryPhoneCode.setCountry(country);
                    countryPhoneCode.setCode(mainCode);
                    repository.save(countryPhoneCode);
                } else {
                    // Сохранение дополнительных кодов
                    for (String additionalCode : additionalCodes) {
                        CountryPhoneCode additionalCountryCode = new CountryPhoneCode();
                        additionalCountryCode.setCountry(country);
                        additionalCountryCode.setCode(mainCode);
                        additionalCountryCode.setAdditionalCode(additionalCode);
                        repository.save(additionalCountryCode);
                    }
                }
            }
        }
    }
}
