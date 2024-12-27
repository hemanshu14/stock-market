package com.stock.exchange.repository;

import com.stock.exchange.repository.model.StockExchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockExchangeRepository extends CrudRepository<StockExchange, Integer> {
    Optional<StockExchange> findByName(String stockExchangeNAme);
}
