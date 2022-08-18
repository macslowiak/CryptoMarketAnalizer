package com.example.cryptoanalizer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "blockchain")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","symbol"})})
public class Blockchain {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @OneToMany(mappedBy = "blockchain", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Cryptocurrency> cryptocurrencies;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String symbol;

}