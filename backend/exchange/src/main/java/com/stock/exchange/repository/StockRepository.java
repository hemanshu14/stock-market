package com.stock.exchange.repository;

import com.stock.exchange.repository.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {
}
