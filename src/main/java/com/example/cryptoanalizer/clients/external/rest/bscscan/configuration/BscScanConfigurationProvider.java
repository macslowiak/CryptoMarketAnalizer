package com.example.cryptoanalizer.clients.external.rest.bscscan.configuration;

import com.example.cryptoanalizer.clients.external.rest.RestApiConfigurationProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@PropertySource("classpath:bscscan-client-configuration.properties")
public class BscScanConfigurationProvider implements RestApiConfigurationProvider {

    @Value("${bscscan.api.key}")
    private String apiKey;

    private final long maxApiCallsPerSecond = 5;

}
