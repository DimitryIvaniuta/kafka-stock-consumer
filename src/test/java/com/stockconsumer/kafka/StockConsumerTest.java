package com.stockconsumer.kafka;


import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.model.repository.StockRepository;
import com.stockconsumer.service.StockConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class StockConsumerTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockConsumer stockConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsume() {
        Stock stock = new Stock(null, "AAPL", 150.0, LocalDateTime.now());

        stockConsumer.consume(stock);

        verify(stockRepository, times(1)).save(stock);
    }
}
