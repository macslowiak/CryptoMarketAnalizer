package com.example.cryptoanalizer.clients.external.rest.bscscan.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BscScanRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Content-Type", "application/json");
        requestTemplate.header("Accept", "application/json");
        requestTemplate.header("Accept-Encoding", "deflate, gzip");
        requestTemplate.header("Something", "something");

    }
}