package com.example.cryptoanalizer.services.objects;

import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlockchainService {

    public static List<Blockchain> getNonNullBlockchainsFrom(List<Cryptocurrency> cryptocurrencies) {
        return cryptocurrencies.stream()
                .map(Cryptocurrency::getBlockchain)
                .filter(Objects::nonNull)
                .toList();
    }

    public static List<Blockchain> getDistinctBlockchainsFrom(List<Blockchain> blockchains) {
        List<Blockchain> distinctBlockchains = new ArrayList<>();

        blockchains.stream()
                .filter(blockchain -> blockchain.getName() != null)
                .collect(Collectors.groupingBy(Blockchain::getName))
                .forEach((blockchainName, duplicatesList) -> distinctBlockchains.add(duplicatesList.get(0)));

        return distinctBlockchains;
    }

    public static List<Blockchain> findNewBlockchainsNotExistingInProvidedRepository(List<Blockchain> blockchainList,
                                                                                     List<Blockchain> repository) {
        blockchainList = getDistinctBlockchainsFrom(blockchainList);

        var blockchainNames = repository.stream()
                .map(Blockchain::getName)
                .toList();

        return blockchainList.stream()
                .filter(blockchain -> !blockchainNames.contains(blockchain.getName()))
                .toList();
    }

}