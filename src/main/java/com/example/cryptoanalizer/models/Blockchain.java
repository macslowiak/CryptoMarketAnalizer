package com.example.cryptoanalizer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "blockchain")
public class Blockchain {

    @Id
    private UUID id;

    @OneToMany(mappedBy = "blockchain", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Cryptocurrency> cryptocurrencies;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String symbol;

}