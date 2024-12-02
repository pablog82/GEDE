package com.babelgroup.gede;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.sql.DataSource;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.babelgroup.gede.service.GestorDocumentalService;

/**
 * The Class GedeApplication.
 */
@SpringBootApplication
@PropertySources({@PropertySource("classpath:application.properties"), @PropertySource("classpath:log4j2.properties"),
        @PropertySource("classpath:database.properties"),
        @PropertySource("file:${spring.config.location}/afirma.properties"),
        @PropertySource("file:${spring.config.location}/gede.properties")
//    @PropertySource("classpath:afirma.properties"),
//    @PropertySource("classpath:gede.properties")
})
public class GedeApplication implements CommandLineRunner {

    @Autowired
    Environment env;

    @Autowired
    private GestorDocumentalService gestorDocumentalService;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GedeApplication.class, args).close();

    }

    /**
     * Run.
     *
     * @param args the args
     * @throws Exception the exception
     */
    @Override
    public void run(String... args) throws Exception {
        gestorDocumentalService.subirDocumentos();
    }

    /**
     * Data source.
     *
     * @return the data source
     */
    @Bean
    DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("sqlite.driver"));
        dataSource.setUrl(env.getProperty("sqlite.url"));
        dataSource.setUsername(env.getProperty("sqlite.user"));
        dataSource.setPassword(env.getProperty("sqlite.password"));
        return dataSource;
    }

    @Bean
    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }
//    public RestTemplate createExternal(boolean sslDisabled) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpComponentsClientHttpRequestFactory requestFactory = getHttpComponentsClientHttpRequestFactory();
//        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));
//        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
//        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
//        restTemplate.setUriTemplateHandler(uriBuilderFactory);
//        return restTemplate;
//    }
//
//    private static HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
//        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial((KeyStore) null, (chain, authType)->true).build();
//        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
//        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(csf).build();
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//        return requestFactory;
//    }

}
