package com.mohitvijayv.map.service;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class HTTPClientService {

    public String get(final String url) throws UnsupportedOperationException, IOException {

        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");

        CloseableHttpClient httpClient =  HttpClientBuilder.create().build();

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            final int responseCode = response.getStatusLine().getStatusCode();

            if(response.getEntity() != null && responseCode >= 200 &&  responseCode < 300) {
                return
                    EntityUtils.toString(response.getEntity());
            }
            else {
                throw new UnsupportedOperationException(String.format("HTTP POST call returned unexpected  response code:%d", responseCode));
            }

        } catch (IOException exception) {
            throw  new IOException(String.format("Unable to fetch credential from service instance: %s", url), exception);
        }
    }
}
