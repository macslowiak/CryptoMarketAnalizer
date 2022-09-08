package com.example.cryptoanalizer.clients.external.rest.bscscan.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BscScanRequest {
    private String module;
    private String action;
    private String address;
    private String tag;
    private int startblock;
    private int endblock;
    private int page;
    private int offset;
    private String sort;
    private String apiKey;
}
