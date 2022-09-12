package com.example.cryptoanalizer.clients.external.rest.bscscan;

import com.example.cryptoanalizer.clients.external.rest.bscscan.configuration.BscScanConfigurationProvider;
import com.example.cryptoanalizer.models.Cryptocurrency;
import feign.Request;
import feign.RequestTemplate;
import feign.codec.DecodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BscScanClientProviderTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    BscScanClient bscScanClient;

    @Mock
    BscScanConfigurationProvider bscScanConfigurationProvider;

    @InjectMocks
    BscScanClientProvider bscScanClientProvider;


    @BeforeEach
    void setUp() {
        bscScanClientProvider = new BscScanClientProvider(bscScanClient, bscScanConfigurationProvider);
    }

    @Test
    void shouldReturnTokenCreationDateFromClientWhenItExistInDatabase() {
        //given
        Long timestampTokenCreation = 1662968933L;
        String address = "0x00000";
        String exampleApiKey = "3213SDAFEASD342";
        given(bscScanConfigurationProvider.getApiKey()).willReturn(exampleApiKey);
        given(bscScanConfigurationProvider.getMaxApiCallsPerSecond()).willReturn(5L);
        given(bscScanClient.getToken(any())
                .getResult()
                .get(0)
                .getContractCreationDate())
                .willReturn(timestampTokenCreation);
        LocalDate expectedTokenCreationDate = LocalDate.of(2022, 9, 12);

        //when
        LocalDate tokenCreationDate = bscScanClientProvider.getTokenCreationDate(address);

        //then
        assertEquals(expectedTokenCreationDate, tokenCreationDate);
    }

    @Test
    void shouldReturnAddressWithTrueValueWhenIsBscAddress() {
        //given
        String exampleApiKey = "3213SDAFEASD342";
        given(bscScanConfigurationProvider.getApiKey()).willReturn(exampleApiKey);
        given(bscScanConfigurationProvider.getMaxApiCallsPerSecond()).willReturn(5L);
        given(bscScanClient.getToken(any())
                .getResult()
                .get(0)
                .getContractCreationDate())
                .willReturn(1111L);
        List<Cryptocurrency> cryptocurrencyListWithBscToken = new ArrayList<>() {{
            add(Cryptocurrency.builder()
                    .address("0x0000")
                    .build());
        }};

        //when
        Map<String, Boolean> addressWithTrueValue = bscScanClientProvider.isBscToken(cryptocurrencyListWithBscToken);

        //then
        assertEquals(true, addressWithTrueValue.get("0x0000"));
    }

    @Test
    void shouldReturnAddressWithFalseValueWhenAddressIsNotBscAddress() {
        //given
        String exampleApiKey = "3213SDAFEASD342";
        Request emptyRequest = Request.create(Request.HttpMethod.GET, "url",
                new HashMap<>(), null, new RequestTemplate());
        List<Cryptocurrency> cryptocurrencyListWithBscToken = new ArrayList<>() {{
            add(Cryptocurrency.builder()
                    .address("0x0000")
                    .build());
        }};

        given(bscScanConfigurationProvider.getApiKey()).willReturn(exampleApiKey);
        given(bscScanConfigurationProvider.getMaxApiCallsPerSecond()).willReturn(5L);
        given(bscScanClient.getToken(any())
                .getResult()
                .get(0)
                .getContractCreationDate())
                .willThrow(new DecodeException(400, "Error! Invalid address format", emptyRequest));


        //when
        Map<String, Boolean> addressWithFalseVale = bscScanClientProvider.isBscToken(cryptocurrencyListWithBscToken);

        //then
        assertEquals(false, addressWithFalseVale.get("0x0000"));
    }

    @Test
    void shouldReturnAddressWithFalseValueWhenTokenHasInvalidAddress() {
        //given
        String exampleApiKey = "3213SDAFEASD342";
        Request emptyRequest = Request.create(Request.HttpMethod.GET, "url",
                new HashMap<>(), null, new RequestTemplate());
        List<Cryptocurrency> cryptocurrencyListWithBscToken = new ArrayList<>() {{
            add(Cryptocurrency.builder()
                    .address("0x0000")
                    .build());
        }};

        given(bscScanConfigurationProvider.getApiKey()).willReturn(exampleApiKey);
        given(bscScanConfigurationProvider.getMaxApiCallsPerSecond()).willReturn(5L);
        given(bscScanClient.getToken(any())
                .getResult()
                .get(0)
                .getContractCreationDate())
                .willThrow(new IndexOutOfBoundsException("Index 0 out of bounds for length 0"));

        //when
        Map<String, Boolean> addressWithFalseVale = bscScanClientProvider.isBscToken(cryptocurrencyListWithBscToken);

        //then
        assertEquals(false, addressWithFalseVale.get("0x0000"));
    }
}