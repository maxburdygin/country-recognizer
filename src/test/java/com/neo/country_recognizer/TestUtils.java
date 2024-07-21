package com.neo.country_recognizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public static Document loadDocumentFromResource(String resourcePath) throws IOException {
        String html = new String(Files.readAllBytes(Paths.get(TestUtils.class.getResource(resourcePath).getPath())));
        return Jsoup.parse(html);
    }
}
