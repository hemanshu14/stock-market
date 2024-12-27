package com.stock.exchange.controller;

import com.stock.exchange.repository.StockRepository;
import com.stock.exchange.repository.model.Stock;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.stock.exchange.config.Constants.STOCK_NOT_FOUND;
import static com.stock.exchange.config.Constants.STOCK_PRICE_UPDATED;

@RequestMapping("/api/v1/stocks")
@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockRepository stockRepository;

    @PostMapping()
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        return ResponseEntity.ok(stockRepository.save(stock));
    }

    @PutMapping()
    public ResponseEntity<String> updateStock(@RequestBody Stock stock) {
        Stock stockFromDb = stockRepository.findById(stock.getId())
                .orElseThrow(() -> new EntityNotFoundException(stock.getName() + STOCK_NOT_FOUND));


        stockFromDb.setCurrentPrice(stock.getCurrentPrice());
        stockRepository.save(stockFromDb);
        return ResponseEntity.ok(stockFromDb.getName() + STOCK_PRICE_UPDATED);
    }

    @GetMapping()
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        stockRepository.findAll().forEach(stocks::add);

        return ResponseEntity.ok(stocks);
    }
}
