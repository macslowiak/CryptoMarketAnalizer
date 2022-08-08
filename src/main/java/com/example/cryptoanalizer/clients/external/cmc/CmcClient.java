package com.example.cryptoanalizer.clients.external.cmc;

import com.example.cryptoanalizer.clients.external.cmc.model.RequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "cmcClient",
        url = "https://pro-api.coinmarketcap.com",
        configuration = CmcClientConfiguration.class)
public interface CmcClient {

    @RequestMapping(method = RequestMethod.GET, value = "{url}")
    RequestDto getAllCryptocurrencies(@PathVariable String url,
                                      @SpringQueryMap Map<String, String> params);

}
