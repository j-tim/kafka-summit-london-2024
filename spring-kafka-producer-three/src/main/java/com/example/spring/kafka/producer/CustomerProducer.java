package com.example.spring.kafka.producer;

import com.example.avro.customer.CustomerForwardDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerProducer {

    private final KafkaTemplate<String, CustomerForwardDemo> kafkaTemplateForward;

    public CustomerProducer(KafkaTemplate<String, CustomerForwardDemo> kafkaTemplateForward) {
        this.kafkaTemplateForward = kafkaTemplateForward;
    }
    public void produce(CustomerForwardDemo customer) {
        kafkaTemplateForward.send("customers-topic-forward", customer);
        log.info("Produced customer: {}", customer);
    }
}
