package com.github.micwan88.tls.mtlstester;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class HttpWebClient {

    Logger logger = LoggerFactory.getLogger(HttpWebClient.class);

    private CloseableHttpClient myHttpClient;
    private String resUrl;
    private String method;

    public HttpWebClient(
            @Value("${mtlstester.client.url:}") String resUrl,
            @Value("${mtlstester.client.method:}") String method,
            @Value("${mtlstester.client.ssl.key-store:}") Resource keystoreRes,
            @Value("${mtlstester.client.ssl.key-store-password:}") String keystorePassword,
            @Value("${mtlstester.client.ssl.key-password:}") String keyPassword,
            @Value("${mtlstester.client.ssl.key-alias:}") String keyAlias,
            @Value("${mtlstester.client.ssl.trust-store:}") Resource truststoreRes,
            @Value("${mtlstester.client.ssl.trust-store-password:}") String truststorePassword
        ) {
        this.resUrl = resUrl;
        this.method = method;

        SSLContextBuilder sslContextBuilder = SSLContexts.custom();
        SSLContext sslContext = null;
        try {
            if (keyAlias != null && !keyAlias.isEmpty()) {
                sslContextBuilder.loadKeyMaterial(keystoreRes.getURL(), keystorePassword.toCharArray(), keyPassword.toCharArray(), (aliases, sslParameters) -> keyAlias);
            } else {
                sslContextBuilder.loadKeyMaterial(keystoreRes.getURL(), keystorePassword.toCharArray(), keyPassword.toCharArray());
            }
        } catch (UnrecoverableKeyException | KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            sslContextBuilder.loadTrustMaterial(truststoreRes.getURL(), truststorePassword.toCharArray());
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException e) {
            e.printStackTrace();
        }

        try {
            sslContext = sslContextBuilder.build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .build();

        final HttpClientConnectionManager httpConnectionMgr = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        this.myHttpClient = HttpClients.custom()
                .setConnectionManager(httpConnectionMgr)
                .build();
    }

    public String execute() {
        try {
            String returnResponse = null;
            if (method.equalsIgnoreCase("POST")) {
                returnResponse = Request.post(resUrl).execute(myHttpClient).returnContent().toString();
            } else if (method.equalsIgnoreCase("PUT")) {
                returnResponse = Request.put(resUrl).execute(myHttpClient).returnContent().toString();
            } else if (method.equalsIgnoreCase("DELETE")) {
                returnResponse = Request.delete(resUrl).execute(myHttpClient).returnContent().toString();
            } else { //GET - default
                returnResponse = Request.get(resUrl).execute(myHttpClient).returnContent().toString();
            }
            return returnResponse;
        } catch (IOException e) {
            logger.error("Got error", e);
        }
        return "Error during request ...";
    }

    @PreDestroy
    public void destroy() {
        if (myHttpClient != null) {
            try {
                myHttpClient.close();
            } catch (IOException e) {
                //Do nothing
            }
        }
    }
}
