package com.example.Products.kafka;

import com.example.Products.constants.ProductConstants;
import com.sharedDto.ImageResponseFromMedia;
import com.sharedDto.RequestingProductImage;
import com.sharedDto.ProductCreatedEvent;
import com.sharedDto.ProductDeletedEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;

import static com.example.Products.constants.ProductConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<String, CompletableFuture<ImageResponseFromMedia>> pendingRequests = new ConcurrentHashMap<>();
//    private final ReplyingKafkaTemplate<String, RequestingProductImage, ImageResponseFromMedia> replyingKafkaTemplate;

    public void sendProductCreatedEvent(ProductCreatedEvent productCreatedEvent) {
        log.info("Sending product created event: {}", productCreatedEvent);
        Message<ProductCreatedEvent> message = MessageBuilder
                .withPayload(productCreatedEvent)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_CREATED)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendProductDeletedEvent(ProductDeletedEvent productDeletedEvent) {
        log.info("Sending product deleted event {}", productDeletedEvent);
        Message<ProductDeletedEvent> message = MessageBuilder
                .withPayload(productDeletedEvent)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_DELETED)
                .build();
        kafkaTemplate.send(message);
    }

    public String requestingProductImages(String productId) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("Requesting Product images for product ID: {}", productId);
        RequestingProductImage request = new RequestingProductImage(productId, "product-image-replies");

        CompletableFuture<ImageResponseFromMedia> future = new CompletableFuture<>();
        pendingRequests.put(productId, future);

        Message<RequestingProductImage> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "product-image-request") // Set the correct request topic
                .build();

        kafkaTemplate.send(message);

        ImageResponseFromMedia response = future.get(10, TimeUnit.SECONDS);
        pendingRequests.remove(productId);
        return response.getImagePath();
    }

    @KafkaListener(topics = "product-image-replies", groupId = "product-service-group")
    public void handleImageResponse(ImageResponseFromMedia response) {
        String productId = response.getProductId();
        CompletableFuture<ImageResponseFromMedia> future = pendingRequests.get(productId);
        if (future!= null) {
            future.complete(response);
        }
    }
}
