//package com.example.spring.kafka.consumer;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaTopicsConfiguration {
//
//    @Bean
//    public NewTopic demoTopicStreams() {
//        return TopicBuilder.name("backward-output-topic")
//            .build();
//    }
//}
