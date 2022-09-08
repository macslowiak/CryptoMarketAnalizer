package com.example.cryptoanalizer.clients.external.rest.cmc.configuration;

import org.springframework.context.annotation.Bean;

public class CmcClientConfiguration {

    private final CmcConfigurationProvider cmcConfigurationProvider;
    public static final int MAX_REQUEST_SIZE = 5000;

    public CmcClientConfiguration(CmcConfigurationProvider cmcConfigurationProvider) {
        this.cmcConfigurationProvider = cmcConfigurationProvider;
    }

    @Bean
    public CmcRequestInterceptor cmcRequestInterceptor() {
        return new CmcRequestInterceptor(cmcConfigurationProvider.getApiKey());
    }
}