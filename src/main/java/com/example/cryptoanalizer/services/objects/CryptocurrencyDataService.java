package com.example.cryptoanalizer.services.objects;

import com.example.cryptoanalizer.models.Cryptocurrency;
import com.example.cryptoanalizer.models.CryptocurrencyData;

import java.util.List;

public class CryptocurrencyDataService {

    public static boolean isCryptoIdAlreadyInProvidedDb(List<CryptocurrencyData> db, Cryptocurrency cryptocurrency) {
        return db.stream()
                .anyMatch(singleCryptoData -> singleCryptoData.getId().equals(cryptocurrency.getId()));
    }
}
