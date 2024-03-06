package com.example.spring.kafka.consumer;

import com.example.avro.customer.CustomerBackwardDemo;
import com.example.avro.customer.CustomerForwardDemo;

public interface CustomerService {


    void handle(CustomerBackwardDemo customer);
    void handle(CustomerForwardDemo customer);
}
