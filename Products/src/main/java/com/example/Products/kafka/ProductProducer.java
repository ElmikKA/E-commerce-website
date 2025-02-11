package com.example.Products.kafka;


import com.sharedDto.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static com.example.Products.constants.ProductConstants.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductProducer {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public void sendProductCreatedEvent(ProductCreatedEvent productCreatedEvent) {
        log.info("Sending product created event: {}", productCreatedEvent);
        Message<ProductCreatedEvent> message = MessageBuilder
                .withPayload(productCreatedEvent)
                        .setHeader(KafkaHeaders.TOPIC, TOPIC)
                        .build();
        kafkaTemplate.send(message);
    }
}
