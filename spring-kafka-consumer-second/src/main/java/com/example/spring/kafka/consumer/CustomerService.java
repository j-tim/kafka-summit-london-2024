package com.example.spring.kafka.consumer;

import com.example.avro.customer.CustomerBackwardDemo;

public interface CustomerService {


    void handle(CustomerBackwardDemo customer);
}
