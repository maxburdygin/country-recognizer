package com.neo.country_recognizer.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultJsoupClient implements JsoupClient {
    @Override
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}

