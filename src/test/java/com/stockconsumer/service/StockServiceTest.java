package com.stockconsumer.service;

import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.model.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link StockServiceImpl}.
 * <p>
 * This class tests the behavior of the StockServiceImpl using Mockito.
 * It verifies the interaction between the service and repository layers.
 */
class StockServiceTest {

    /**
     * Mocked instance of the StockRepository.
     * Used to simulate database operations without connecting to an actual database.
     */
    @Mock
    private StockRepository stockRepository;

    /**
     * The service under test, which is injected with mocked dependencies.
     */
    @InjectMocks
    private StockServiceImpl stockService;

    /**
     * Sets up the mocks and initializes the service before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link StockServiceImpl#getAllStocks()} method.
     * <p>
     * Scenario: When the repository returns a list of stocks,
     * the service should return the same list without modification.
     */
    @Test
    void testGetAllStocks() {
        List<Stock> mockStocks = List.of(
                new Stock(1L, "AAPL", 150.0, LocalDateTime.now()),
                new Stock(2L, "MSFT", 320.5, LocalDateTime.now())
        );

        when(stockRepository.findAll()).thenReturn(mockStocks);

        List<Stock> result = stockService.getAllStocks();

        assertEquals(2, result.size());
        verify(stockRepository, times(1)).findAll();
    }
}
