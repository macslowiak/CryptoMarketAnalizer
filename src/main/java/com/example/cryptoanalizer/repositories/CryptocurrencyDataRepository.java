package com.example.cryptoanalizer.repositories;

import com.example.cryptoanalizer.models.CryptocurrencyData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CryptocurrencyDataRepository extends JpaRepository<CryptocurrencyData, UUID> {
}
