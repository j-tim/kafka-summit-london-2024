package com.example.spring.kafka.consumer;

import com.example.avro.customer.CustomerForwardDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("forward")
public class CustomersForwardConsumer {
    private final CustomerService customerService;

    public CustomersForwardConsumer(CustomerService customerService) {
        log.info("Initialized CustomersForwardConsumer");
        this.customerService = customerService;
    }

    @KafkaListener(topics = {"customers-topic-forward"}, idIsGroup = false)
    public void on2(CustomerForwardDemo customer, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
        log.info("Consumed from partition: {} value: {}", partition, customer);
        customerService.handle(customer);
    }
}
