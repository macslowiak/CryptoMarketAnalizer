package com.example.cryptoanalizer.clients.external.rest.cmc.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CmcRequestInterceptor implements RequestInterceptor {
    private final String cmcApiKey;

    public CmcRequestInterceptor(String cmcApiKey) {
        this.cmcApiKey = cmcApiKey;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Content-Type", "application/json");
        requestTemplate.header("Accept", "application/json");
        requestTemplate.header("Accept-Encoding", "deflate, gzip");
        requestTemplate.header("X-CMC_PRO_API_KEY", cmcApiKey);
    }
}
