package com.example.cryptoanalizer.services.database;

import com.example.cryptoanalizer.clients.external.rest.bscscan.BscScanClientProvider;
import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;
import com.example.cryptoanalizer.models.CryptocurrencyData;
import com.example.cryptoanalizer.repositories.BlockchainRepository;
import com.example.cryptoanalizer.repositories.CryptocurrencyDataRepository;
import com.example.cryptoanalizer.repositories.CryptocurrencyRepository;
import com.example.cryptoanalizer.services.objects.CryptocurrencyDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BscScanFetcherService implements CryptoFetcherService {

    private static final int AMOUNT_OF_OBJECTS_IN_PACKAGED_REQUESTS = 100;
    private final BscScanClientProvider bscScanClientProvider;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final CryptocurrencyDataRepository cryptocurrencyDataRepository;
    private final BlockchainRepository blockchainRepository;

    public BscScanFetcherService(BscScanClientProvider bscScanClientProvider, CryptocurrencyRepository cryptocurrencyRepository, CryptocurrencyDataRepository cryptocurrencyDataRepository, BlockchainRepository blockchainRepository) {
        this.bscScanClientProvider = bscScanClientProvider;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.cryptocurrencyDataRepository = cryptocurrencyDataRepository;
        this.blockchainRepository = blockchainRepository;
    }

    @Override
    public void fetchCryptoDataToDb() {
        excludeBscTokensFromBnbBlockchain();
        fetchBscTokensCreationDates();
    }

    /*
    Some coins can be attached to BNB blockchain, however they are in BSC blockchain.
    This method sort them out and create BSC blockchain if not exists.
     */
    public void excludeBscTokensFromBnbBlockchain() {
        List<Cryptocurrency> bnbChainCryptocurrencies = cryptocurrencyRepository.findByBlockchain_Symbol("BNB");
        int firstObjectFromPackage = 0;
        int maxTries = 5;
        int tries = 0;

        saveBscBlockchainIfNotExists();
        Blockchain bscBlockchain = blockchainRepository.findBySymbol("BSC");
        log.info("Separating BSC addresses from BNB and addresses with wrong format..." +
                "Operation can take around: " + bnbChainCryptocurrencies.size() / 100 + " minutes");

        do {
            try {
                List<Cryptocurrency> packagedCryptocurrencies = bnbChainCryptocurrencies.subList(firstObjectFromPackage,
                        Math.min((firstObjectFromPackage + AMOUNT_OF_OBJECTS_IN_PACKAGED_REQUESTS),
                                bnbChainCryptocurrencies.size()));


                Map<String, Boolean> mappedTokens = bscScanClientProvider.isBscToken(packagedCryptocurrencies);

                packagedCryptocurrencies.forEach(cryptocurrency -> {
                    if (mappedTokens.get(cryptocurrency.getAddress())) {
                        cryptocurrency.setBlockchain(bscBlockchain);
                    }
                });

                cryptocurrencyRepository.saveAll(bnbChainCryptocurrencies);
                firstObjectFromPackage += AMOUNT_OF_OBJECTS_IN_PACKAGED_REQUESTS;
                tries = 0;

                /*
                Added because of the api problem (also on bsc) - https://github.com/pcko1/etherscan-python/issues/31
                TODO: When issue will be solved - refactor this method (note: issue appear with many calls)
                */
            } catch (NullPointerException exception) {

                try {
                    Thread.sleep(1000);
                    tries++;
                    log.info("Got null pointer exception. Retry operation: " + tries + " attempt/" + maxTries);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (tries == maxTries) {
                    firstObjectFromPackage += AMOUNT_OF_OBJECTS_IN_PACKAGED_REQUESTS;
                    tries = 0;
                    log.warn("To much tries. Items: " + firstObjectFromPackage + " were skipped because" +
                            " one of the items was empty");
                }
            }
        } while (firstObjectFromPackage < bnbChainCryptocurrencies.size());

        log.info("Addresses separated and saved to database.");
    }

    public void fetchBscTokensCreationDates() {
        List<Cryptocurrency> bscChainCryptocurrencies = cryptocurrencyRepository.findByBlockchain_Symbol("BSC");
        List<CryptocurrencyData> cryptocurrencyData = cryptocurrencyDataRepository.findAll();

        log.info("Getting BSC tokens creation dates... " +
                "Operation can take around: " + (bscChainCryptocurrencies.size() - cryptocurrencyData.size()) / 90 + " minutes");
        for (Cryptocurrency cryptocurrency : bscChainCryptocurrencies) {
            if (!CryptocurrencyDataService.isCryptoIdAlreadyInDatabase(cryptocurrencyData, cryptocurrency)) {
                LocalDate tempDate = bscScanClientProvider.getTokenCreationDate(cryptocurrency.getAddress());
                CryptocurrencyData cryptocurrencyDataToSave = CryptocurrencyData.builder()
                        .id(cryptocurrency.getId())
                        .cryptocurrency(cryptocurrency)
                        .creationDate(tempDate)
                        .build();
                cryptocurrencyData.add(cryptocurrencyDataToSave);
            }
        }
        cryptocurrencyDataRepository.saveAll(cryptocurrencyData);
        log.info("BSC tokens creation dates were saved to proper token data");
    }

    private void saveBscBlockchainIfNotExists() {
        if (blockchainRepository.findBySymbol("BSC") == null) {
            blockchainRepository.save(Blockchain.builder()
                    .name("Binance Smart Chain")
                    .symbol("BSC")
                    .build());
        }
    }
}
