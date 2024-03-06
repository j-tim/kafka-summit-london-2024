package com.example.spring.kafka.consumer;

import com.example.avro.customer.CustomerBackwardDemo;
import com.example.avro.customer.CustomerForwardDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public void handle(CustomerBackwardDemo customer) {
        log.info("Received customer event: {}", customer);
    }

    @Override
    public void handle(CustomerForwardDemo customer) {
        log.info("Received customer event: {}", customer);
    }
}
