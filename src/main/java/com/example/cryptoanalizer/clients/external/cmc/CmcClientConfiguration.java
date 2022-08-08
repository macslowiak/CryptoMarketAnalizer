package com.example.cryptoanalizer.clients.external.cmc;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:cmc-client-configuration.properties")
public class CmcClientConfiguration {

    public static final int MAX_REQUEST_SIZE = 5000;
    @Value("${private.api.key}")
    private String cmcApiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Accept-Encoding", "deflate, gzip");
            requestTemplate.header("X-CMC_PRO_API_KEY", cmcApiKey);
        };
    }
}