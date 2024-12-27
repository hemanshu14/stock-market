package com.stock.exchange.controller;

import com.stock.exchange.repository.StockExchangeRepository;
import com.stock.exchange.repository.StockRepository;
import com.stock.exchange.repository.model.Stock;
import com.stock.exchange.repository.model.StockExchange;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.stock.exchange.config.Constants.STOCK_EXCHANGE_NOT_FOUND;
import static com.stock.exchange.config.Constants.STOCK_NOT_FOUND;

@RequestMapping("/api/v1/stock-exchanges")
@RestController
@RequiredArgsConstructor
public class StockExchangeController {

    private final StockExchangeRepository stockExchangeRepository;
    private final StockRepository stockRepository;

    @PostMapping("/{name}")
    public ResponseEntity<String> addStockToStockExchange(@PathVariable String name, @RequestBody Stock stock) {
        StockExchange stockExchange = stockExchangeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(name + STOCK_EXCHANGE_NOT_FOUND));
        Stock stockFromDb = stockRepository.findById(stock.getId())
                .orElseThrow(() -> new EntityNotFoundException(STOCK_NOT_FOUND));

        stockExchange.getStocks().add(stockFromDb);
        if (stockExchange.getStocks().size() >= 5) {
            stockExchange.setLiveInMarket(true);
        }
        stockFromDb.getStockExchanges().add(stockExchange);
        stockExchangeRepository.save(stockExchange);
        stockRepository.save(stockFromDb);
        return ResponseEntity.ok("Stock" + " " + stockFromDb.getName() + "has been added to" + " " + stockExchange.getName());

    }

    @GetMapping("/{name}")
    public ResponseEntity<List<Stock>> getStocksByStockExchangeName(@PathVariable String name) {
        StockExchange stockExchange = stockExchangeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(name + STOCK_EXCHANGE_NOT_FOUND));

        return ResponseEntity.ok(stockExchange.getStocks().stream().toList());
    }

    @GetMapping()
    public ResponseEntity<List<StockExchange>> getStockExchanges() {
        List<StockExchange> stockExchanges = new ArrayList<>();
        stockExchangeRepository.findAll().forEach(stockExchanges::add);

        return ResponseEntity.ok(stockExchanges);
    }
}

