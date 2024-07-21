package com.neo.country_recognizer.util;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface JsoupClient {
    Document getDocument(String url) throws IOException;
}

