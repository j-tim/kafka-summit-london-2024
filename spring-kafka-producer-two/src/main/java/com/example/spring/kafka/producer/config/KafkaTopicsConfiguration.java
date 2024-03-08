package com.example.spring.kafka.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfiguration {

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.enabled", havingValue = "true")
    public NewTopic backwardDemoTopic() {
        return TopicBuilder.name("customers-topic-backward")
            .build();
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.enabled", havingValue = "true")
    public NewTopic forwardDemoTopic() {
        return TopicBuilder.name("customers-topic-forward")
                .build();
    }
    @Bean
    public NewTopic demoTopicStreams() {
        return TopicBuilder.name("forward-output-topic")
            .build();
    }
}
