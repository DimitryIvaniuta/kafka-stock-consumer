package com.stockconsumer.model.repository;


import com.stockconsumer.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Stock} entities.
 * <p>
 * This interface provides methods for performing CRUD operations on the "stocks" table.
 * It extends {@link JpaRepository}, which provides standard methods such as save, findById, findAll, and delete.
 * <p>
 * Custom query methods are also defined for fetching stocks by symbol and finding the highest-priced stocks.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {


    /**
     * Finds the stocks by its symbol.
     * <p>
     * Since stock symbols are unique, this method returns an {@link Optional} containing the stock
     * if found, or an empty {@link Optional} if no stock matches the given symbol.
     *
     * @param symbol the symbol of the stock (e.g., "AAPL" for Apple Inc.)
     * @return a list of Stocks
     */
    List<Stock> findBySymbol(String symbol);

    /**
     * Finds all stocks with a price greater than the specified value.
     * <p>
     * This method returns a list of stocks that are currently priced above the provided price.
     *
     * @param price the minimum price threshold
     * @return a {@link List} of {@link Stock} objects with prices greater than the specified value
     */
    List<Stock> findByPriceGreaterThan(Double price);

    /**
     * Finds all stocks sorted by their price in descending order.
     * <p>
     * This method is useful for displaying the highest-priced stocks at the top.
     *
     * @return a {@link List} of {@link Stock} objects sorted by price in descending order
     */
    List<Stock> findAllByOrderByPriceDesc();

}
