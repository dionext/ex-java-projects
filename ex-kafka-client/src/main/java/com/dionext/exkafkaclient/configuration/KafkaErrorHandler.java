package com.dionext.exkafkaclient.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Slf4j
public class KafkaErrorHandler extends DefaultErrorHandler {

    public KafkaErrorHandler(){
        super(((consumerRecord, e) -> {
            log.error("Kafka error", e);
        }));
    }

}
