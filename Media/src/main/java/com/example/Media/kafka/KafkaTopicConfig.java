package com.example.Media.kafka;

import com.example.Media.constants.MediaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic productCreated() {
        return TopicBuilder
                .name(MediaConstants.TOPIC_IMAGE_PATH_RESPONSE)
                .build();
    }
}
