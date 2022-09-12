package com.example.cryptoanalizer.clients.external.rest.bscscan;

import com.example.cryptoanalizer.clients.external.rest.bscscan.configuration.BscScanClientConfiguration;
import com.example.cryptoanalizer.clients.external.rest.bscscan.models.BscScanRequest;
import com.example.cryptoanalizer.clients.external.rest.bscscan.models.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "bscScanClient",
        url = "https://api.bscscan.com/api",
        configuration = BscScanClientConfiguration.class)
public interface BscScanClient {
    @RequestMapping(method = RequestMethod.GET)
    TransactionDto getToken(@SpringQueryMap BscScanRequest requestParams);

}
