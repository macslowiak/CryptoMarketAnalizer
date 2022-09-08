package com.example.cryptoanalizer.clients.external.rest.bscscan;


import com.example.cryptoanalizer.clients.external.rest.bscscan.configuration.BscScanConfigurationProvider;
import com.example.cryptoanalizer.clients.external.rest.bscscan.models.BscScanRequest;
import com.example.cryptoanalizer.models.Cryptocurrency;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Component
@Slf4j
public class BscScanClientProvider {
    private final BscScanClient bscScanClient;
    private final BscScanConfigurationProvider bscScanConfigurationProvider;

    public BscScanClientProvider(BscScanClient bscScanClient, BscScanConfigurationProvider bscScanConfigurationProvider) {
        this.bscScanClient = bscScanClient;
        this.bscScanConfigurationProvider = bscScanConfigurationProvider;
    }

    public LocalDate getTokenCreationDate(String address) {

        BscScanRequest requestParams = BscScanRequest.builder()
                .module("account")
                .action("txlist")
                .address(address)
                .startblock(0)
                .endblock(99999999)
                .page(1)
                .offset(1)
                .sort("asc")
                .apiKey(bscScanConfigurationProvider.getApiKey())
                .build();

        apiCallsRateLimiter();
        Long timeStamp = bscScanClient.getToken(requestParams)
                .getResult()
                .get(0)
                .getContractCreationDate();
        log.debug("Timestamp (creation of token): " + timeStamp);
        return LocalDate.ofInstant(Instant.ofEpochSecond(timeStamp), TimeZone.getDefault().toZoneId());

    }

    public Map<String, Boolean> isBscToken(List<Cryptocurrency> cryptocurrenciesToScan) {
        Map<String, Boolean> addressesWithResult = new HashMap<>();

        List<String> addressesToScan = cryptocurrenciesToScan.stream()
                .map(Cryptocurrency::getAddress)
                .toList();

        for (String address : addressesToScan) {
            BscScanRequest requestParams = BscScanRequest.builder()
                    .module("account")
                    .action("txlist")
                    .address(address)
                    .startblock(0)
                    .endblock(99999999)
                    .page(1)
                    .offset(1)
                    .sort("asc")
                    .apiKey(bscScanConfigurationProvider.getApiKey())
                    .build();

            try {
                apiCallsRateLimiter();
                var result = bscScanClient.getToken(requestParams)
                        .getResult()
                        .get(0)
                        .getContractCreationDate();
                addressesWithResult.put(address, true);

            } catch (DecodeException exception) {
                if (exception.getMessage().contains("Error! Invalid address format")) {
                    log.debug("Address is not BSC address: " + address + " Returning possible earliest date");
                    addressesWithResult.put(address, false);
                } else {
                    throw exception;
                }
            } catch (IndexOutOfBoundsException exception) {
                if (exception.getMessage().contains("Index 0 out of bounds for length 0")) {
                    log.debug("Invalid address: " + address);
                    addressesWithResult.put(address, false);
                } else {
                    throw exception;
                }
            }
        }
        return addressesWithResult;
    }

    private void apiCallsRateLimiter() {
        try {
            Thread.sleep(1000 / bscScanConfigurationProvider.getMaxApiCallsPerSecond());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
