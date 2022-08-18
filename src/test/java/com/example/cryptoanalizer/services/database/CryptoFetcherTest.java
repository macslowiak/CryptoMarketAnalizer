package com.example.cryptoanalizer.services.database;


import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CryptoFetcherTest {

    CryptoFetcher cryptoFetcher;

    @BeforeEach
    void setUp() {
        cryptoFetcher = Mockito.mock(CryptoFetcher.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    void shouldReturnMappedCryptocurrencyWitchBlockchainSymbolWhenMapCryptocurrencyWithBlockchains() {
        //given
        Cryptocurrency cryptocurrency = Cryptocurrency.builder()
                .name("Ethereum")
                .symbol("ETH")
                .blockchain(Blockchain
                        .builder()
                        .name("ValidBlockchain")
                        .build())
                .build();

        List<Cryptocurrency> cryptocurrenciesToSave = new ArrayList<>() {{
            add(cryptocurrency);
        }};

        List<Blockchain> blockchainsFromDb = new ArrayList<>() {{
            add(Blockchain.builder().name("ValidBlockchain").symbol("VALID").build());
        }};

        //when
        List<Cryptocurrency> cryptocurrencyListResult = cryptoFetcher.mapCryptocurrenciesWithBlockchainsInDb(
                cryptocurrenciesToSave, blockchainsFromDb);

        String mappedBlockchainSymbol = cryptocurrencyListResult.get(0).getBlockchain().getSymbol();

        //then
        assertEquals("VALID", mappedBlockchainSymbol);
    }


    @Test
    void shouldReturnCryptocurrencyWithNullBlockchainWhenMapCryptocurrencyWithoutBlockchains() {
        //given
        Cryptocurrency cryptocurrency = Cryptocurrency.builder()
                .name("Ethereum")
                .symbol("ETH")
                .blockchain(null)
                .build();

        List<Cryptocurrency> cryptocurrenciesToSave = new ArrayList<>() {{
            add(cryptocurrency);
        }};

        List<Blockchain> blockchainsFromDb = new ArrayList<>() {{
            add(Blockchain.builder().name("ValidBlockchain").symbol("VALID").build());
        }};

        //when
        List<Cryptocurrency> cryptocurrencyListResult = cryptoFetcher.mapCryptocurrenciesWithBlockchainsInDb(
                cryptocurrenciesToSave, blockchainsFromDb);

        //then
        assertNull(cryptocurrencyListResult.get(0).getBlockchain());
    }
}