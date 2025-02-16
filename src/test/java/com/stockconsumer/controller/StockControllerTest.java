package com.stockconsumer.controller;

import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.model.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetAllStocks() throws Exception {
        stockRepository.save(new Stock(null, "AAPL", 150.0, LocalDateTime.now()));
        stockRepository.save(new Stock(null, "MSFT", 320.5, LocalDateTime.now()));

        mockMvc.perform(get("/stocks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));
    }

    @Test
    void testGetStocksBySymbol() throws Exception {
        stockRepository.save(new Stock(null, "AAPL", 150.0, LocalDateTime.now()));

        mockMvc.perform(get("/stocks/AAPL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));
    }

}
