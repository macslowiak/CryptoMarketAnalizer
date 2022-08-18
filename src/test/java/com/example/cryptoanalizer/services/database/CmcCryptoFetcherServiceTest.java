package com.example.cryptoanalizer.services.database;

import com.example.cryptoanalizer.clients.external.cmc.CryptoCmcClientProvider;
import com.example.cryptoanalizer.clients.external.cmc.model.CmcListingStatus;
import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;
import com.example.cryptoanalizer.repositories.BlockchainRepository;
import com.example.cryptoanalizer.repositories.CryptocurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CmcCryptoFetcherServiceTest {

    @Mock
    CryptoCmcClientProvider cryptoCmcClientProvider;

    @Mock
    CryptocurrencyRepository cryptocurrencyRepository;

    @Mock
    BlockchainRepository blockchainRepository;

    @InjectMocks
    CmcCryptoFetcherService cmcCryptoFetcherService;

    @BeforeEach
    void setUp() {
        cmcCryptoFetcherService = new CmcCryptoFetcherService(
                cryptoCmcClientProvider,
                cryptocurrencyRepository,
                blockchainRepository);
    }

    @Test
    void shouldPassWhenAllMethodsWereCalled() {
        List<Blockchain> blockchainList = new ArrayList<>();
        List<Cryptocurrency> cryptocurrencyList = new ArrayList<>();

        //given
        given(cryptoCmcClientProvider.getCryptocurrenciesWithStatus(any(CmcListingStatus.class)))
                .willReturn(cryptocurrencyList);
        given(cryptocurrencyRepository.findAll()).willReturn(cryptocurrencyList);
        given(blockchainRepository.findAll()).willReturn(blockchainList);

        //when
        cmcCryptoFetcherService.fetchCryptoDataToDb();

        //then
        verify(blockchainRepository, times(1)).saveAll(any());
        verify(cryptocurrencyRepository, times(1)).saveAll(any());
        verify(cryptoCmcClientProvider, times(3)).getCryptocurrenciesWithStatus(any());
    }
}