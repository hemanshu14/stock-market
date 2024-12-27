package com.stock.exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.exchange.repository.StockExchangeRepository;
import com.stock.exchange.repository.StockRepository;
import com.stock.exchange.repository.model.Stock;
import com.stock.exchange.repository.model.StockExchange;
import com.stock.exchange.service.AuthenticationService;
import com.stock.exchange.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.stock.exchange.config.Constants.STOCK_EXCHANGE_NOT_FOUND;
import static com.stock.exchange.config.Constants.STOCK_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StockExchangeControllerTest {
    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private StockExchangeRepository stockExchangeRepository;
    @MockBean
    private StockRepository stockRepository;


    @Test
    @DisplayName("Testing getting stocks attached to a stock exchange with Bearer token")
    public void givenStocksInAStockExchangeWhileFetchingItExpectedCorrectResponse() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setName("Facebook");
        stock.setDescription("facebook co.");
        stock.setCurrentPrice(24.8);

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName("NASDAQ");
        stockExchange.setDescription("NASDAQ");
        stockExchange.setLiveInMarket(true);
        stockExchange.setStocks(Set.of(stock));

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockExchangeRepository.findByName(anyString())).thenReturn(Optional.of(stockExchange));

        //then
        mockMvc.perform(get("/api/v1/stock-exchanges/NASDAQ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Testing getting stocks attached to a stock exchange with Bearer token getting exception")
    public void givenStocksInAStockExchangeWhileFetchingItExpectedException() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setName("Facebook");
        stock.setDescription("facebook co.");
        stock.setCurrentPrice(24.8);

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName("NASDAQ");
        stockExchange.setDescription("NASDAQ");
        stockExchange.setLiveInMarket(true);
        stockExchange.setStocks(Set.of(stock));

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockExchangeRepository.findByName(anyString())).thenThrow(new EntityNotFoundException(STOCK_EXCHANGE_NOT_FOUND));

        //then
        mockMvc.perform(get("/api/v1/stock-exchanges/NASDAQ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Testing getting stock exchanges from DB with Bearer token")
    public void gettingStockExchangesFromDbExpectedCorrectResponse() throws Exception {
        //given
        StockExchange stockExchange = new StockExchange();
        stockExchange.setName("NASDAQ");
        stockExchange.setDescription("NASDAQ");
        stockExchange.setLiveInMarket(true);

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockExchangeRepository.findAll()).thenReturn(List.of(stockExchange));

        //then
        mockMvc.perform(get("/api/v1/stock-exchanges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Testing adding a stock to a stock exchange with Bearer token")
    public void givenStockDetailsToAddInAStockExchangeExpectedCorrectResponse() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCurrentPrice(22.8);

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName("NASDAQ");
        stockExchange.setDescription("NASDAQ");
        stockExchange.setLiveInMarket(true);

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockRepository.findById(anyInt())).thenReturn(Optional.of(stock));
        when(stockExchangeRepository.findByName(anyString())).thenReturn(Optional.of(stockExchange));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(stockExchange);

        //then
        mockMvc.perform(post("/api/v1/stock-exchanges/NASDAQ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing adding a stock to a stock exchange with Bearer token getting no stock found exception")
    public void givenStockDetailsToAddInAStockExchangeExpectedNoStockFoundException() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCurrentPrice(22.8);

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName("NASDAQ");
        stockExchange.setDescription("NASDAQ");
        stockExchange.setLiveInMarket(true);

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockRepository.findById(anyInt())).thenThrow(new EntityNotFoundException(STOCK_NOT_FOUND));
        when(stockExchangeRepository.findByName(anyString())).thenReturn(Optional.of(stockExchange));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(stockExchange);

        //then
        mockMvc.perform(post("/api/v1/stock-exchanges/NASDAQ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isNotFound());
    }
}
