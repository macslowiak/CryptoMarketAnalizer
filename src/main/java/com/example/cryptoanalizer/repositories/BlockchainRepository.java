package com.example.cryptoanalizer.repositories;

import com.example.cryptoanalizer.models.Blockchain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockchainRepository extends JpaRepository<Blockchain, UUID> {
    Blockchain findBySymbol(String symbol);

}