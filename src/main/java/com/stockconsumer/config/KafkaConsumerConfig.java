package com.stockconsumer.config;


import com.stockconsumer.model.entity.Stock;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka consumer.
 * <p>
 * This class sets up the Kafka consumer factory, listener container factory,
 * and error handling strategies for processing stock price updates.
 */
@Configuration
@Slf4j
public class KafkaConsumerConfig {

    /**
     * Default address of the Kafka broker.
     */
    private static final String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";

    /**
     * Group ID for stock price consumers.
     */
    private static final String KAFKA_GROUP_ID = "stock-consumer-group";

    /**
     * Kafka auto offset reset policy: "earliest" means the consumer will start reading from the earliest available message.
     */
    private static final String KAFKA_AUTO_OFFSET_RESET = "earliest";

    /**
     * Maximum number of retry attempts for processing a Kafka message.
     */
    private static final int RETRY_MAX_ATTEMPTS = 3;

    /**
     * Delay duration (in milliseconds) between retry attempts.
     */
    private static final Long RETRY_DELAY_MILLISECONDS = 1000L;

    /**
     * Trusted package pattern for JSON deserialization.
     * Accepts all packages to allow Kafka to deserialize messages into Stock objects.
     */
    private static final String JSON_TRUSTED_PACKAGES = "*";

    /**
     * Configures the Kafka {@link ConsumerFactory} for deserializing Stock messages.
     * <p>
     * It defines the Kafka consumer properties such as group ID, deserialization settings,
     * and auto offset reset behavior.
     * Configuring the Kafka Consumer with connection and serialization settings.
     * Creating Consumer instances that Spring Kafka uses for consuming messages.
     *
     * @return a factory that produces Kafka consumers, a configured {@link ConsumerFactory} instance.
     */
    @Bean
    public ConsumerFactory<String, Stock> consumerFactory() {
        JsonDeserializer<Stock> deserializer = new JsonDeserializer<>(Stock.class);
        Map<String, Object> props = new HashMap<>();


        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KAFKA_AUTO_OFFSET_RESET);

        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);

        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");


        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                deserializer
        );
    }

    /**
     * Configures a {@link ConcurrentKafkaListenerContainerFactory} for handling Kafka messages.
     * <p>
     * Sets the consumer factory and assigns a common error handler to handle exceptions during processing.
     *
     * @return a configured {@link ConcurrentKafkaListenerContainerFactory} instance.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Stock> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Stock> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(RETRY_MAX_ATTEMPTS);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        factory.setCommonErrorHandler(commonErrorHandler());
        factory.setBatchListener(false);
        factory.setConcurrency(1);

        return factory;
    }


    /**
     * Provides a common error handler with a fixed backoff retry strategy.
     * <p>
     * In case of errors during Kafka message processing, the consumer will retry
     * with a fixed delay for a limited number of attempts before logging the error.
     *
     * @return a {@link CommonErrorHandler} configured with retry logic.
     */
    @Bean
    public CommonErrorHandler commonErrorHandler() {
        return new DefaultErrorHandler(
                (consumerRecord, exception) -> log.error("Error processing record with key {}: {}, due to: {}",
                        consumerRecord.key(), consumerRecord.value(), exception.getMessage()),
                new FixedBackOff(RETRY_DELAY_MILLISECONDS, RETRY_MAX_ATTEMPTS)
        );
    }

}
