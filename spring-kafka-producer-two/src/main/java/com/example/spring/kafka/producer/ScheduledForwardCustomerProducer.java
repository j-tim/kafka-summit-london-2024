package com.example.spring.kafka.producer;

import com.example.avro.customer.CustomerForwardDemo;
import com.example.spring.kafka.producer.generator.RandomCustomerGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("forward")
@ConditionalOnProperty(name = "kafka.producer.enabled", havingValue = "true")
public class ScheduledForwardCustomerProducer {

    private final CustomerProducer producer;
    private final RandomCustomerGenerator generator;

    public ScheduledForwardCustomerProducer(CustomerProducer producer, RandomCustomerGenerator generator) {
        this.producer = producer;
        this.generator = generator;
    }

    @Scheduled(fixedRateString = "${kafka.producer.rate}")
    public void produce2() {
        log.info("Producing to forward topic");
        CustomerForwardDemo customer = generator.generateRandomCustomerForward();
        producer.produce(customer);
    }
}
