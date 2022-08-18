package com.example.cryptoanalizer.services.objects;

import com.example.cryptoanalizer.models.Cryptocurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CryptocurrencyService {

    public static List<Cryptocurrency> getDistinctCryptocurrenciesFrom(List<Cryptocurrency> cryptocurrencies) {
        List<Cryptocurrency> distinctCryptocurrencies = new ArrayList<>();

        cryptocurrencies.stream()
                .filter(cryptocurrency -> cryptocurrency.getName() != null)
                .collect(Collectors.groupingBy(k -> Arrays.asList(k.getName(), k.getSymbol(), k.getAddress())))
                .forEach((blockchainName, duplicatesList) -> distinctCryptocurrencies.add(duplicatesList.get(0)));

        return distinctCryptocurrencies;
    }

    public static List<Cryptocurrency> findNewCryptoNotExistingInProvidedRepository(List<Cryptocurrency> cryptocurrencyList,
                                                                                    List<Cryptocurrency> repository) {

        cryptocurrencyList = getDistinctCryptocurrenciesFrom(cryptocurrencyList);

        var cryptoNames = repository.stream()
                .map(Cryptocurrency::getName)
                .toList();

        var cryptoSymbol = repository.stream()
                .map(Cryptocurrency::getSymbol)
                .toList();

        var cryptoAddress = repository.stream()
                .map(Cryptocurrency::getAddress)
                .toList();

        return cryptocurrencyList.stream()
                .filter(cryptocurrency -> !cryptoNames.contains(cryptocurrency.getName()) ||
                        !cryptoSymbol.contains(cryptocurrency.getSymbol()) ||
                        !cryptoAddress.contains(cryptocurrency.getAddress())
                )
                .toList();
    }
}