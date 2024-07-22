package com.neo.country_recognizer.service;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.util.JsoupClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class WikipediaDataLoader {

    private static final String WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes#Alphabetical_order";
    private static final Logger logger = LoggerFactory.getLogger(WikipediaDataLoader.class);

    @Autowired
    private GuavaDataCache cache;

    @Autowired
    private JsoupClient jsoupClient;

    @EventListener(ContextRefreshedEvent.class)
    @Scheduled(cron = "0 * * * * ?")
    public void loadCountryCodes() throws IOException {
        updateCountryCodes();
    }

    // Выполняется каждую минуту
    public void updateCountryCodes() throws IOException {
        logger.info("Cron job started");
        Document doc = jsoupClient.getDocument(WIKIPEDIA_URL);
        Element table = doc.select("table.wikitable").first();
        Elements rows = table.select("tr");

        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.size() > 1) {
                String country = cols.get(0).text();
                String codes = cols.get(1).text();

                // Извлечение основного кода и дополнительных кодов
                String[] codeParts = codes.split("\\D+");
                String mainCode = codeParts[0];
                String[] additionalCodes = Arrays.copyOfRange(codeParts, 1, codeParts.length);


                if (additionalCodes.length == 0) {
                    CountryPhoneCode countryPhoneCode = new CountryPhoneCode();
                    countryPhoneCode.setCountry(country);
                    countryPhoneCode.setCode(mainCode);
                    cache.addCountryPhoneCode(countryPhoneCode);
                } else {
                    for (String additionalCode : additionalCodes) {
                        CountryPhoneCode additionalCountryCode = new CountryPhoneCode();
                        additionalCountryCode.setCountry(country);
                        additionalCountryCode.setCode(mainCode);
                        additionalCountryCode.setAdditionalCode(additionalCode);
                        cache.addCountryPhoneCode(additionalCountryCode);
                    }
                }
            }
        }
        logger.info("Cron job finished");
    }
}
