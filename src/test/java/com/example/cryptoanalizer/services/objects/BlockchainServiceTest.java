package com.example.cryptoanalizer.services.objects;

import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockchainServiceTest {

    @Test
    void shouldReturnNonNullBlockchainsWhenProvidedCryptocurrencyList() {
        //given
        List<Cryptocurrency> cryptocurrencyList = new ArrayList<>() {{
            add(Cryptocurrency.builder().blockchain(null).build());
            add(Cryptocurrency.builder().blockchain(new Blockchain()).build());
            add(Cryptocurrency.builder().blockchain(new Blockchain()).build());
        }};

        //when
        List<Blockchain> nonNullBlockchains = BlockchainService.getNonNullBlockchainsFrom(cryptocurrencyList);

        //then
        assertEquals(2, nonNullBlockchains.size());
    }

    @Test
    void shouldReturnDistinctBlockchainsByNameWhenProvidedBlockchainList() {
        //given
        List<Blockchain> blockchainList = new ArrayList<>() {{
            add(Blockchain.builder().name("Bitcoin").build());
            add(Blockchain.builder().name("Bitcoin").build());
            add(Blockchain.builder().name(null).build());
            add(Blockchain.builder().name("Ethereum").build());
        }};

        //when
        List<Blockchain> distinctBlockchains = BlockchainService.getDistinctBlockchainsFrom(blockchainList);

        //then
        assertEquals(2, distinctBlockchains.size());
    }

    @Test
    void shouldReturnNewBlockchainsWhenComparingProvidedListWithRepositoryByBlockchainName() {
        //given
        List<Blockchain> blockchainList = new ArrayList<>() {{
            add(Blockchain.builder().name("Avalanche").build());
            add(Blockchain.builder().name("Bitcoin").build());
            add(Blockchain.builder().name("Ethereum").build());
        }};

        List<Blockchain> repository = new ArrayList<>() {{
            add(Blockchain.builder().name("Bitcoin").build());
            add(Blockchain.builder().name("Ethereum").build());
        }};

        //when
        List<Blockchain> newBlockchains = BlockchainService.findNewBlockchainsNotExistingInProvidedRepository(
                blockchainList,
                repository);

        //then
        assertEquals("Avalanche", newBlockchains.get(0).getName());
    }
}