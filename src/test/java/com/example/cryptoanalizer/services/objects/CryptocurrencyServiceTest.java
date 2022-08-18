package com.example.cryptoanalizer.services.objects;


import com.example.cryptoanalizer.models.Cryptocurrency;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CryptocurrencyServiceTest {

    @Test
    void shouldReturnDistinctCryptocurrenciesByCryptoNameAndSymbolAndAddressWhenProvidedCryptocurrencyList() {
        //given
        List<Cryptocurrency> cryptocurrencyList = new ArrayList<>() {{
            //acceptable
            add(Cryptocurrency.builder().name("Bitcoin").symbol(null).address(null).build());
            add(Cryptocurrency.builder().name("Other").symbol("BTC").address(null).build());
            add(Cryptocurrency.builder().name("Bitcoin").symbol("BTC").build());
            add(Cryptocurrency.builder().name("Ethereum").build());
            //dropped
            add(Cryptocurrency.builder().name("Bitcoin").symbol(null).address(null).build());
            add(Cryptocurrency.builder().name(null).build());
        }};

        //when
        List<Cryptocurrency> distinctCryptocurrencies = CryptocurrencyService.getDistinctCryptocurrenciesFrom(
                cryptocurrencyList);

        //then
        assertEquals(4, distinctCryptocurrencies.size());
    }

    @Test
    void shouldReturnNewCryptocurrenciesWhenComparingProvidedListWithRepositoryByCryptoNameAndSymbolAndAddress() {
        //given
        List<Cryptocurrency> repository = new ArrayList<>() {{
            add(Cryptocurrency.builder().name("Avalanche").symbol("AVAX").address("0x000").build());
            add(Cryptocurrency.builder().name("Bitcoin").symbol("BTC").address("0x100").build());
            add(Cryptocurrency.builder().name("Ethereum").symbol("ETH").address("0x200").build());
        }};

        List<Cryptocurrency> cryptocurrencyList = new ArrayList<>() {{
            //existing
            add(Cryptocurrency.builder().name("Avalanche").symbol("AVAX").address("0x000").build());
            //new
            add(Cryptocurrency.builder().name("Bitcoin").symbol("BTCx").address("0x100").build());
            add(Cryptocurrency.builder().name("SomethingNew").symbol("SMTH").address("0x0900").build());
            add(Cryptocurrency.builder().name("SomethingNew2").symbol("SMTH2").address("0x0910").build());
            //existing
            add(Cryptocurrency.builder().name("SomethingNew").symbol("SMTH").address("0x0900").build());
        }};


        //when
        List<Cryptocurrency> newCryptocurrencies = CryptocurrencyService.findNewCryptoNotExistingInProvidedRepository(
                cryptocurrencyList,
                repository);

        //then
        assertEquals(3, newCryptocurrencies.size());
    }
}