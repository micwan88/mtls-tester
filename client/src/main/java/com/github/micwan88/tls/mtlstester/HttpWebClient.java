package com.github.micwan88.tls.mtlstester;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HttpWebClient {
    @Value("${mtlstester.client.url}")
    private String resUrl;

    private WebClient myWebClient;

    public HttpWebClient() throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream("D:\\MichaelWan\\pinmgr\\docker\\cert\\client.jks"), "keystore2".toCharArray());
        
        KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(new FileInputStream("D:\\MichaelWan\\pinmgr\\docker\\cert\\truststore.jks"), "truststore".toCharArray());

        sslContextFactory.setKeyStore(keystore);
        sslContextFactory.setKeyStorePassword("keystore2");
        sslContextFactory.setTrustStore(truststore);
        sslContextFactory.setTrustStorePassword("truststore");



        HttpClient httpClient = new HttpClient(sslContextFactory);
        ClientHttpConnector connector = new JettyClientHttpConnector(httpClient);

        this.myWebClient = WebClient.builder()
            .clientConnector(connector)
            .build();
    }

    public String get() {
        return myWebClient.get().uri(resUrl).retrieve().bodyToMono(String.class).block();
    }
}
