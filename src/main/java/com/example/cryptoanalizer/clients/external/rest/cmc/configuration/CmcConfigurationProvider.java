package com.example.cryptoanalizer.clients.external.rest.cmc.configuration;

import com.example.cryptoanalizer.clients.external.rest.RestApiConfigurationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:cmc-client-configuration.properties")
public class CmcConfigurationProvider implements RestApiConfigurationProvider {

    @Value("${cmc.api.key}")
    private String apiKey;

    @Override
    public String getApiKey() {
        return apiKey;
    }
}
