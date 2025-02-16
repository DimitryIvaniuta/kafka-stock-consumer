package com.stockconsumer.service;

import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.model.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link StockService} for managing stock operations.
 * Handles business logic and communicates with the database via {@link StockRepository}.
 */
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    /**
     * Repository for performing CRUD operations on stocks.
     */
    private final StockRepository stockRepository;

    /**
     * Retrieves all stocks from the database.
     *
     * @return a list of all {@link Stock} objects.
     */
    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    /**
     * Retrieves a stock by its unique identifier.
     *
     * @param id the unique identifier of the stock.
     * @return an {@link Optional} containing the {@link Stock} if found, or empty if not.
     */
    @Override
    public Optional<Stock> getStockById(final Long id) {
        return stockRepository.findById(id);
    }

    /**
     * Stock Symbol
     *
     * @param symbol the unique identifier of the stock.
     * @return a list of Stocks
     */
    @Override
    public List<Stock> getStocksBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    /**
     * Creates a new stock and saves it to the database.
     *
     * @param stock the {@link Stock} object to create.
     * @return the created {@link Stock} object.
     */
    @Override
    public Stock createStock(final Stock stock) {
        return stockRepository.save(stock);
    }

    /**
     * Updates an existing stock identified by its ID.
     *
     * @param id the unique identifier of the stock to update.
     * @param stock the updated {@link Stock} object.
     * @return an {@link Optional} containing the updated {@link Stock}, or empty if the stock is not found.
     */
    @Override
    public Optional<Stock> updateStock(final Long id, final Stock stock) {
        return stockRepository.findById(id)
                .map(existingStock -> {
                    existingStock.setSymbol(stock.getSymbol());
                    existingStock.setPrice(stock.getPrice());
                    return stockRepository.save(existingStock);
                });
    }

    /**
     * Deletes a stock identified by its unique identifier.
     *
     * @param id the unique identifier of the stock to delete.
     * @return {@code true} if the stock was deleted successfully, {@code false} otherwise.
     */
    @Override
    public boolean deleteStock(final Long id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
