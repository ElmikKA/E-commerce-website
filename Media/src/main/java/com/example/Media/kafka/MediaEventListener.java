package com.example.Media.kafka;

import com.example.Media.service.IMediaService;

import com.sharedDto.ProductCreatedEvent;
import com.sharedDto.ProductDeletedEvent;
import com.sharedDto.RequestingProductImage;
import com.sharedDto.ImageResponseFromMedia;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class MediaEventListener {
    private final IMediaService mediaService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "product-created", groupId = "media-service-group")
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        try {
            mediaService.uploadMedia(event.getImageData(), event.getProductId());// Acknowledge message processing
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "product-deleted", groupId = "media-service-group")
    public void handleProductDeletedEvent(ProductDeletedEvent event) {
        try {
            mediaService.deleteMedia(event.getProductId());
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "product-image-request", groupId = "media-service-group")
    public void handleProductImageRequestEvent(RequestingProductImage event) {
        log.info("Handling product image request event: {}", event);
        String imagePath = mediaService.fetchMediaByProductId(event.getProductId());
        String replyTopic = event.getReplyTopic();

        ImageResponseFromMedia imageResponseFromMedia = new ImageResponseFromMedia(event.getProductId(), imagePath);
        Message<ImageResponseFromMedia> message = MessageBuilder
                .withPayload(imageResponseFromMedia)
                .setHeader(KafkaHeaders.TOPIC, replyTopic)
                .build();

        log.info("Sending image response: {}", imageResponseFromMedia);
        kafkaTemplate.send(message);
    }
}
