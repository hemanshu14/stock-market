package com.stock.exchange.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Table(name = "stockexchange")
@Entity
@Getter
@Setter
public class StockExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "liveinmarket", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean liveInMarket = false;

    @ManyToMany(mappedBy = "stockExchanges", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private Set<Stock> stocks;
}
