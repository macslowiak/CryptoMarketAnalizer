package com.example.cryptoanalizer.clients.external.cmc;

import com.example.cryptoanalizer.clients.external.rest.cmc.CmcClient;
import com.example.cryptoanalizer.clients.external.rest.cmc.CryptoCmcClientProvider;
import com.example.cryptoanalizer.clients.external.rest.cmc.models.CmcListingStatus;
import com.example.cryptoanalizer.clients.external.rest.cmc.models.CryptocurrencyDto;
import com.example.cryptoanalizer.clients.external.rest.cmc.models.PlatformDto;
import com.example.cryptoanalizer.clients.external.rest.cmc.models.RequestDto;
import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CryptoCmcClientProviderTest {

    @Mock
    CmcClient cmcClient;

    @InjectMocks
    CryptoCmcClientProvider cryptoCmcClientProvider;

    @BeforeEach
    void setUp() {
        cryptoCmcClientProvider = new CryptoCmcClientProvider(cmcClient);
    }


    @Test
    void shouldPassWhenProperCmcListingStatusesAreProvided() {
        //given
        List<CmcListingStatus> listingStatuses = new ArrayList<>() {{
            add(CmcListingStatus.ACTIVE);
            add(CmcListingStatus.INACTIVE);
            add(CmcListingStatus.UNTRACKED);
        }};
        List<Cryptocurrency> list = new ArrayList<>();
        RequestDto requestDto = Mockito.mock(RequestDto.class);
        given(cmcClient.getAllCryptocurrencies(any(), any())).willReturn(requestDto);

        //when and then
        for (CmcListingStatus status : listingStatuses) {
            cryptoCmcClientProvider.getCryptocurrenciesWithStatus(status);
        }

    }

    @Test
    void shouldCreateCryptocurrencyListWhenCryptocurrencyDtoListIsProvided() {
        //given
        CryptocurrencyDto cryptocurrencyDto = CryptocurrencyDto.builder()
                .name("Bitcoin")
                .symbol("BTC")
                .platform(PlatformDto.builder()
                        .name("Bitcoin")
                        .symbol("BTC")
                        .tokenAddress("0x00000000")
                        .build())
                .build();

        Cryptocurrency cryptocurrency = Cryptocurrency.builder()
                .name("Bitcoin")
                .symbol("BTC")
                .blockchain(Blockchain.builder()
                        .name("Bitcoin")
                        .symbol("BTC")
                        .build())
                .address("0x00000000")
                .build();


        List<CryptocurrencyDto> cryptocurrencyDtoList = new ArrayList<>() {{
            add(cryptocurrencyDto);
        }};

        //when
        List<Cryptocurrency> cryptocurrencyList = cryptoCmcClientProvider
                .createCryptocurrencyFrom(cryptocurrencyDtoList);

        //then
        assertEquals(1, cryptocurrencyList.size());
        assertEquals(cryptocurrency.getName(), cryptocurrencyList.get(0).getName());
        assertEquals(cryptocurrency.getBlockchain().getSymbol(), cryptocurrencyList.get(0).getBlockchain().getSymbol());
        assertEquals(cryptocurrency.getAddress(), cryptocurrencyList.get(0).getAddress());

    }

    @Test
    void shouldReturnEmptyListWhenCryptocurrencyDtoListIsNullOrEmpty() {
        //given
        List<CryptocurrencyDto> emptyList = new ArrayList<>();
        List<CryptocurrencyDto> nullList = null;

        //when
        List<Cryptocurrency> emptyResult = cryptoCmcClientProvider.createCryptocurrencyFrom(emptyList);
        List<Cryptocurrency> nullResult = cryptoCmcClientProvider.createCryptocurrencyFrom(nullList);

        //then
        assertEquals(0, emptyResult.size());
        assertEquals(0, nullResult.size());
    }
}