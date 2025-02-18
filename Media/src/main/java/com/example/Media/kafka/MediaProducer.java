package com.example.Media.kafka;

import com.example.Media.constants.MediaConstants;
import com.sharedDto.ImageResponseFromMedia;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MediaProducer {

    private final KafkaTemplate<String, ImageResponseFromMedia> kafkaTemplate;

    public void sendProductImageEvent(ImageResponseFromMedia imageResponseFromMedia) {
        log.info("Sending product image event {}", imageResponseFromMedia);
        Message<ImageResponseFromMedia> message = MessageBuilder
                .withPayload(imageResponseFromMedia)
                .setHeader(KafkaHeaders.TOPIC, MediaConstants.TOPIC_IMAGE_PATH_RESPONSE)
                .build();
        kafkaTemplate.send(message);
    }
}
