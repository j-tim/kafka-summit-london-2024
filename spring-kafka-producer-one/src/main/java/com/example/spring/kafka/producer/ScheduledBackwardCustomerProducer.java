package com.example.spring.kafka.producer;

import com.example.avro.customer.CustomerBackwardDemo;
import lombok.extern.slf4j.Slf4j;
import com.example.spring.kafka.producer.generator.RandomCustomerGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("backward")
@ConditionalOnProperty(name = "kafka.producer.enabled", havingValue = "true")
public class ScheduledBackwardCustomerProducer {

    private final CustomerProducer producer;
    private final RandomCustomerGenerator generator;

    public ScheduledBackwardCustomerProducer(CustomerProducer producer, RandomCustomerGenerator generator) {
        this.producer = producer;
        this.generator = generator;
    }

    @Scheduled(fixedRateString = "${kafka.producer.rate}")
    public void produce() {
        log.info("Producing to backward topic");
        CustomerBackwardDemo customer = generator.generateRandomCustomer();
        producer.produce(customer);
    }
}
