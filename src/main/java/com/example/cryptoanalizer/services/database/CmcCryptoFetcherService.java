package com.example.cryptoanalizer.services.database;

import com.example.cryptoanalizer.clients.external.rest.cmc.CryptoCmcClientProvider;
import com.example.cryptoanalizer.clients.external.rest.cmc.models.CmcListingStatus;
import com.example.cryptoanalizer.models.Cryptocurrency;
import com.example.cryptoanalizer.repositories.BlockchainRepository;
import com.example.cryptoanalizer.repositories.CryptocurrencyRepository;
import com.example.cryptoanalizer.services.objects.BlockchainService;
import com.example.cryptoanalizer.services.objects.CryptocurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CmcCryptoFetcherService extends CryptoFetcher implements CryptoFetcherService {
    private final CryptoCmcClientProvider cryptoCmcClientProvider;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final BlockchainRepository blockchainRepository;


    public CmcCryptoFetcherService(CryptoCmcClientProvider cryptoCmcClientProvider,
                                   CryptocurrencyRepository cryptocurrencyRepository,
                                   BlockchainRepository blockchainRepository) {
        this.cryptoCmcClientProvider = cryptoCmcClientProvider;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.blockchainRepository = blockchainRepository;
    }


    @Override
    @Transactional
    public void fetchCryptoDataToDb() {

        List<Cryptocurrency> cryptocurrencyList = new ArrayList<>() {{
            addAll(cryptoCmcClientProvider.getCryptocurrenciesWithStatus(CmcListingStatus.ACTIVE));
            addAll(cryptoCmcClientProvider.getCryptocurrenciesWithStatus(CmcListingStatus.UNTRACKED));
            addAll(cryptoCmcClientProvider.getCryptocurrenciesWithStatus(CmcListingStatus.INACTIVE));
        }};

        var cryptocurrenciesFromDb = cryptocurrencyRepository.findAll();
        var blockchainsFromDb = blockchainRepository.findAll();

        var cryptocurrenciesToSave = CryptocurrencyService
                .findNewCryptoNotExistingInProvidedRepository(cryptocurrencyList, cryptocurrenciesFromDb);

        var blockchainsFromClient = BlockchainService.getNonNullBlockchainsFrom(cryptocurrenciesToSave);

        var blockchainsToSave = BlockchainService
                .findNewBlockchainsNotExistingInProvidedRepository(blockchainsFromClient, blockchainsFromDb);

        log.info("Saving new blockchains to database...");

        blockchainRepository.saveAll(blockchainsToSave);

        blockchainsFromDb.addAll(blockchainsToSave);
        cryptocurrenciesToSave = mapCryptocurrenciesWithBlockchainsInDb(cryptocurrenciesToSave,
                blockchainsFromDb);

        log.info("Saving new cryptocurrencies to database...");

        cryptocurrencyRepository.saveAll(cryptocurrenciesToSave);

    }

}