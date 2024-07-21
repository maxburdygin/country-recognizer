package com.neo.country_recognizer.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DefaultJsoupClient implements JsoupClient {
    @Override
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}

