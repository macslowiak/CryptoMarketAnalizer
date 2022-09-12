package com.example.cryptoanalizer.services.database;

import com.example.cryptoanalizer.clients.external.rest.bscscan.BscScanClientProvider;
import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;
import com.example.cryptoanalizer.models.CryptocurrencyData;
import com.example.cryptoanalizer.repositories.BlockchainRepository;
import com.example.cryptoanalizer.repositories.CryptocurrencyDataRepository;
import com.example.cryptoanalizer.repositories.CryptocurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BscScanFetcherServiceTest {

    @Mock
    BscScanClientProvider bscScanClientProvider;

    @Mock
    CryptocurrencyRepository cryptocurrencyRepository;

    @Mock
    CryptocurrencyDataRepository cryptocurrencyDataRepository;

    @Mock
    BlockchainRepository blockchainRepository;

    @InjectMocks
    BscScanFetcherService bscScanFetcherService;

    @BeforeEach
    void setUp() {
        bscScanFetcherService = new BscScanFetcherService(
                bscScanClientProvider,
                cryptocurrencyRepository,
                cryptocurrencyDataRepository,
                blockchainRepository
        );
    }

    @Test
    void shouldCallProperMethodsWhenFetchCryptoDataToDb() {
        //given
        BscScanFetcherService bscScanFetcherServiceSpy = Mockito.spy(new BscScanFetcherService(
                bscScanClientProvider,
                cryptocurrencyRepository,
                cryptocurrencyDataRepository,
                blockchainRepository
        ));

        //when
        bscScanFetcherServiceSpy.fetchCryptoDataToDb();

        //then
        verify(bscScanFetcherServiceSpy).fetchBscTokensCreationDates();
        verify(bscScanFetcherServiceSpy).excludeBscTokensFromBnbBlockchain();
    }

    @Test
    void shouldChangeToBscBlockchainOfBscTokensFromBnbListWhenProvideListWithTwoBscTokens() {
        //given
        Blockchain bscBlockchain = Blockchain.builder()
                .id(UUID.fromString("7ab8756e-14c3-4315-a970-4dc931ca5407"))
                .name("Binance Smart Chain")
                .symbol("BSC")
                .build();
        List<Cryptocurrency> bnbChainCryptocurrencies = new ArrayList<>() {{
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("b9375ba3-029f-487a-8e36-35c89e5442d3"))
                    .address("0x000bsc")
                    .build());
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("e794d6e6-2483-4fd6-b683-ae0fda8873d3"))
                    .address("0x001bsc")
                    .build());
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("0c4ec10d-2482-4ed2-b261-cd8d7cca4085"))
                    .address("0x002bnb")
                    .build());
        }};
        given(cryptocurrencyRepository.findByBlockchain_Symbol("BNB"))
                .willReturn(bnbChainCryptocurrencies);
        given(blockchainRepository.findBySymbol("BSC"))
                .willReturn(bscBlockchain);
        given(bscScanClientProvider.isBscToken(any()))
                .willReturn(Map.of("0x000bsc", true, "0x001bsc", true, "0x002bnb", false));

        //when
        bscScanFetcherService.excludeBscTokensFromBnbBlockchain();

        //then
        verify(cryptocurrencyRepository).saveAll(any());
    }

    @Test
    void shouldTryToSaveBscBlockchainFiveTimesWhenNullPointerExceptionIsThrown() {
        //given
        Blockchain bscBlockchain = Blockchain.builder()
                .id(UUID.fromString("7ab8756e-14c3-4315-a970-4dc931ca5407"))
                .name("Binance Smart Chain")
                .symbol("BSC")
                .build();
        List<Cryptocurrency> bnbChainCryptocurrencies = new ArrayList<>() {{
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("b9375ba3-029f-487a-8e36-35c89e5442d3"))
                    .address("0x000bsc")
                    .build());
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("b9375ba3-029f-487a-8e36-35c89e5442d3"))
                    .address("0x000bsc")
                    .build());
        }};
        given(cryptocurrencyRepository.findByBlockchain_Symbol("BNB"))
                .willReturn(bnbChainCryptocurrencies);
        given(blockchainRepository.findBySymbol("BSC"))
                .willReturn(bscBlockchain);
        given(bscScanClientProvider.isBscToken(any()))
                .willThrow(NullPointerException.class);

        //when
        bscScanFetcherService.excludeBscTokensFromBnbBlockchain();

        //then
        verify(bscScanClientProvider, times(5)).isBscToken(any());
    }

    @Test
    void shouldAddCreationDatesForTwoTokensWhenTwoOfThreeDoNotHaveTheCreationDate() {
        //given
        List<CryptocurrencyData> existingTokensWithData = new ArrayList<>() {{
            add(CryptocurrencyData.builder()
                    .id(UUID.fromString("b9375ba3-029f-487a-8e36-35c89e5442d3"))
                    .build());
        }};
        List<Cryptocurrency> bscChainCryptocurrencies = new ArrayList<>() {{
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("b9375ba3-029f-487a-8e36-35c89e5442d3"))
                    .address("0x000")
                    .build());
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("e794d6e6-2483-4fd6-b683-ae0fda8873d3"))
                    .address("0x001")
                    .build());
            add(Cryptocurrency.builder()
                    .id(UUID.fromString("e1f06e6c-5190-4796-93b3-35478fc262a4"))
                    .address("0x002")
                    .build());
        }};
        given(cryptocurrencyRepository.findByBlockchain_Symbol("BSC"))
                .willReturn(bscChainCryptocurrencies);
        given(cryptocurrencyDataRepository.findAll())
                .willReturn(existingTokensWithData);
        given(bscScanClientProvider.getTokenCreationDate(any()))
                .willReturn(LocalDate.of(1111, 11, 11));

        //when
        bscScanFetcherService.fetchBscTokensCreationDates();

        //then
        verify(cryptocurrencyRepository).findByBlockchain_Symbol(any());
        verify(cryptocurrencyDataRepository).findAll();
        verify(bscScanClientProvider, times(2)).getTokenCreationDate(any());
        verify(cryptocurrencyDataRepository).saveAll(any());
        assertEquals(3, existingTokensWithData.size());
    }
}