package com.example.cryptoanalizer.repositories;

import com.example.cryptoanalizer.models.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, UUID> {
    List<Cryptocurrency> findByBlockchain_Symbol(String blockchainName);
}