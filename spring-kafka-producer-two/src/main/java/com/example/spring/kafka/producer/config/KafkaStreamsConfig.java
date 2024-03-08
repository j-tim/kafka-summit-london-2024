package com.example.spring.kafka.producer.config;

import com.example.avro.customer.CustomerForwardDemo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.KafkaStreamBrancher;

@Slf4j
@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    public KafkaStreamsConfig(){
        log.info("Initialized KafkaStreamsConfig");
    }

    @Bean
    public KStream<String, CustomerForwardDemo> kStream(StreamsBuilder streamsBuilder,
        @Value("${merak-kafka.example.producer.topicName}") String inputTopicName,
        @Value("${merak-kafka.example.streams.output.topicName}") String outputTopicName) {

        KStream<String, CustomerForwardDemo> stream = streamsBuilder.stream(inputTopicName);
        stream.print(Printed.toSysOut());

        KStream<String, CustomerForwardDemo> branchedStream = new KafkaStreamBrancher<String, CustomerForwardDemo>()
            .branch((key, value) -> value.getSchema() != null, kStream -> kStream.to(outputTopicName))
            .defaultBranch(kStream -> kStream.to(outputTopicName))
            .onTopOf(streamsBuilder.stream(inputTopicName));

        branchedStream.print(Printed.toSysOut());
        return branchedStream;
    }
}
