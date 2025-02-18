package com.example.Products.kafka;

import com.sharedDto.ImageResponseFromMedia;
import com.sharedDto.RequestingProductImage;
import com.sharedDto.ProductCreatedEvent;
import com.sharedDto.ProductDeletedEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.Products.constants.ProductConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
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

//    public String requestingProductImages(RequestingProductImage requestingProductImage) {
//        log.info("Requesting Product images for product ID: {}", requestingProductImage.getProductId());
//
//        String correlationId = UUID.randomUUID().toString();
//        String replyTopic = "product-image-response-topic";
//
//        Message<RequestingProductImage> message = MessageBuilder
//                .withPayload(requestingProductImage)
//                .setHeader(KafkaHeaders.TOPIC, TOPIC_GETTING_IMAGE)
//                .setHeader(KafkaHeaders.REPLY_TOPIC, replyTopic)
//                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
//                .build();
//
//        try {
//            RequestReplyMessageFuture<String, RequestingProductImage> future =
//                    replyingKafkaTemplate.sendAndReceive(message);
//
//            // Explicitly handle the cast
//            Message<?> rawResponseMessage = future.get(10, TimeUnit.SECONDS);
//
//            // Validate the payload type
//            if (rawResponseMessage.getPayload() instanceof ImageResponseFromMedia response) {
//                return response.getImagePath();
//            } else {
//                throw new IllegalStateException("Unexpected response type: "
//                        + rawResponseMessage.getPayload().getClass());
//            }
//
//        } catch (TimeoutException e) {
//            log.error("Timeout fetching image for product {}: {}",
//                    requestingProductImage.getProductId(), e.getMessage());
//            throw new RuntimeException("Image service timeout", e);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            log.error("Request interrupted for product {}: {}",
//                    requestingProductImage.getProductId(), e.getMessage());
//            throw new RuntimeException("Image request interrupted", e);
//        } catch (ExecutionException e) {
//            log.error("Execution failed for product {}: {}",
//                    requestingProductImage.getProductId(), e.getMessage());
//            throw new RuntimeException("Image service error", e.getCause());
//        }
//    }
}
