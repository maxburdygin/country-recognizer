package com.neo.country_recognizer.integration;

import com.neo.country_recognizer.model.CountryPhoneCode;
import com.neo.country_recognizer.repository.CountryPhoneCodeRepository;
import com.neo.country_recognizer.service.CountryPhoneCodeService;
import com.neo.country_recognizer.service.GuavaDataCache;
import com.neo.country_recognizer.service.WikipediaDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CountryPhoneCodeServiceIntegrationTest {

    @MockBean
    private WikipediaDataLoader wikipediaDataLoader;

    @Autowired
    private CountryPhoneCodeService service;

    @Autowired
    private CountryPhoneCodeRepository repository;

    @Autowired
    private GuavaDataCache cache;

    @BeforeEach
    public void setUp() throws Exception {
        repository.deleteAll();
        cache.clear();
        List<CountryPhoneCode> cacheData = loadDataFromXml("/integration/data-cache.xml");
        cacheData.forEach(code -> cache.addCountryPhoneCode(code));

        List<CountryPhoneCode> repoData = loadDataFromXml("/integration/data-repository.xml");
        repository.saveAll(repoData);
    }

    private List<CountryPhoneCode> loadDataFromXml(String resourcePath) throws Exception {
        List<CountryPhoneCode> countryCodes = new ArrayList<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("countryCode");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    CountryPhoneCode countryPhoneCode = new CountryPhoneCode();
                    countryPhoneCode.setCountry(element.getElementsByTagName("country").item(0).getTextContent());
                    countryPhoneCode.setCode(element.getElementsByTagName("code").item(0).getTextContent());
                    if (element.getElementsByTagName("additionalCode").getLength() > 0) {
                        countryPhoneCode.setAdditionalCode(element.getElementsByTagName("additionalCode").item(0).getTextContent());
                    }

                    countryCodes.add(countryPhoneCode);
                }
            }
        }
        return countryCodes;
    }

    @Test
    public void testFindCountryByPhoneNumberInCache() {
        String phoneNumber = "79146486117";
        List<CountryPhoneCode> results = service.findCountryByPhoneNumber(phoneNumber);

        // Verify the expected country code(s)
        assertEquals(1, results.size());
        assertEquals("RU", results.get(0).getCountry());
        assertEquals("7", results.get(0).getCode());
    }

    @Test
    public void testFindCountryByPhoneNumberInDb() {
        String phoneNumber = "86123456789"; // China code
        List<CountryPhoneCode> results = service.findCountryByPhoneNumber(phoneNumber);

        // Verify the expected country code(s)
        assertEquals(1, results.size());
        assertEquals("CN", results.get(0).getCountry());
        assertEquals("86", results.get(0).getCode());
    }

    @Test
    public void testFindCountryByPhoneNumberNotFound() {
        String phoneNumber = "99912345";
        List<CountryPhoneCode> results = service.findCountryByPhoneNumber(phoneNumber);
        System.out.println(results);
        // Verify that no country codes are found
        assertEquals(0, results.size());
    }
}
