package com.example.Products.kafka;

import com.example.Products.constants.ProductConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic productCreated() {
        return TopicBuilder
                .name(ProductConstants.TOPIC_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productDeleted() {
        return TopicBuilder
                .name(ProductConstants.TOPIC_DELETED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic getProductImage() {
        return TopicBuilder
                .name(ProductConstants.TOPIC_GETTING_IMAGE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productImageResponse() {
        return TopicBuilder
                .name("product-image-response-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
