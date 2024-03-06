package com.example.spring.kafka.consumer;

import com.example.avro.customer.CustomerBackwardDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("backward")
public class CustomersBackwardConsumer {
    private final CustomerService customerService;

    public CustomersBackwardConsumer(CustomerService customerService) {
        log.info("Initialized CustomersBackwardConsumer");
        this.customerService = customerService;
    }


    @KafkaListener(topics = {"customers-topic-backward"}, idIsGroup = false)
    public void on(CustomerBackwardDemo customer, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
        log.info("Consumed from partition: {} value: {}", partition, customer);
        customerService.handle(customer);
    }
}
