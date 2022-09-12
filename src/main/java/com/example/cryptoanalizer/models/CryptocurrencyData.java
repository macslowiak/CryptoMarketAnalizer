package com.example.cryptoanalizer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "cryptocurrencyData")
@Table(name = "cryptocurrency_data")
public class CryptocurrencyData {

    @Id
    private UUID id;

    @MapsId
    @OneToOne
    private Cryptocurrency cryptocurrency;

    @Column(name = "creation_date")
    private LocalDate creationDate;
}
