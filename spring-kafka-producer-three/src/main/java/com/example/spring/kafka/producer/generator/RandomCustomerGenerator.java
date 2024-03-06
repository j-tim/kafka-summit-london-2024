package com.example.spring.kafka.producer.generator;

import com.example.avro.customer.CustomerForwardDemo;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

@Service
public class RandomCustomerGenerator {

    private final Faker faker;


    public RandomCustomerGenerator() {
        this.faker = new Faker();
    }

    public CustomerForwardDemo generateRandomCustomerForward() {
        return CustomerForwardDemo.newBuilder()
                .setName(faker.name().nameWithMiddle())
                .setDateOfBirth(faker.date().birthday().toString())
                .build();
    }
}
