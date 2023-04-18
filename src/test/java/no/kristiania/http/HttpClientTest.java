package no.kristiania.http;

import no.kristiania.http.HttpClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {

    @Test
    void getResponseCode200() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void getResponseCode404() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/blahblah");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void returnHeaders() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    void readBody() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertTrue(client.getMessageBody().startsWith("<!DOCTYPE html>"),
                "Expected HTML: " + client.getMessageBody());
    }
}