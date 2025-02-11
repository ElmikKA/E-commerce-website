package com.example.Media.kafka;

import com.example.Media.service.IMediaService;
import com.example.basedomains.ProductCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class MediaEventListener {
    private final IMediaService mediaService;

    @KafkaListener(topics = "product-created", groupId = "media-service-group")
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        try {
            mediaService.uploadMedia(event.getImageData(), event.getProductId());// Acknowledge message processing
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }
}
