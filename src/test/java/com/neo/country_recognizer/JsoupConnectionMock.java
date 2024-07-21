package com.neo.country_recognizer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsoupConnectionMock {

    public static void mockConnectGet(Document document) throws IOException {
        Connection connection = mock(Connection.class);
        Connection.Response response = mock(Connection.Response.class);
        when(response.body()).thenReturn(document.outerHtml());
        when(connection.execute()).thenReturn(response);
        when(connection.get()).thenReturn(document);
        when(Jsoup.connect(anyString())).thenReturn(connection);
    }
}

