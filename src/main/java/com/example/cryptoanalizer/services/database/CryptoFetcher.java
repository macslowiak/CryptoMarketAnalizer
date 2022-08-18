package com.example.cryptoanalizer.services.database;

import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;

import java.util.List;

public abstract class CryptoFetcher {

    public List<Cryptocurrency> mapCryptocurrenciesWithBlockchainsInDb(List<Cryptocurrency> cryptocurrenciesToSave,
                                                                       List<Blockchain> blockchainsFromDb) {
        cryptocurrenciesToSave
                .forEach(cryptocurrency -> {
                    if (cryptocurrency.getBlockchain() != null) {
                        cryptocurrency.setBlockchain(
                                blockchainsFromDb.stream()
                                        .filter(blockchain -> blockchain.getName().equals(
                                                cryptocurrency.getBlockchain().getName()
                                        ))
                                        .findFirst()
                                        .get()
                        );
                    }
                });

        return cryptocurrenciesToSave;
    }
}
