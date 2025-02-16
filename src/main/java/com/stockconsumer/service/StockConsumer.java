package com.stockconsumer.service;

import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.model.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    /**
     * Stock repository.
     */
    private final StockRepository stockRepository;

    /**
     * Automatically called by the Spring Kafka framework
     * whenever a message is published to the specified Kafka topic.
     * Method should listen for messages from a Kafka topic = "stock-prices".
     * Subscribes the consumer to this Kafka topic.
     * groupId = "stock-consumer-group": Associates this listener with a consumer group.
     * Kafka ensures that messages are distributed among consumers in the same group.
     * When a producer publishes a message to the stock-prices topic,
     * Kafka sends that message to all consumers in the stock-consumer-group.
     * Where KafkaListener Deserializes the message (from JSON) into a Stock object.
     * Calls consume() method, passing the Stock object.
     *
     * @param stock entity
     */
    @KafkaListener(topics = "stock-prices", groupId = "stock-consumer-group")
    public void consume(final Stock stock) {
        log.info("Consumed stock update: {}", stock);
        stockRepository.save(stock);
    }

}
