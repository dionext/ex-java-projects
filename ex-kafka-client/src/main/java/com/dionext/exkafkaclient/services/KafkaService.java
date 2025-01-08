package com.dionext.exkafkaclient.services;

import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {

    @Value("${server.port:8080}")
    private String port;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplate restTemplate;
    @Value("${spring.kafka.consumer.topic.mykafka}")
    private String topicName;
    @KafkaListener(
            topics = "${spring.kafka.consumer.topic.mykafka}",
            groupId = "${spring.kafka.consumer.group}",
            containerFactory = "setListenerContainerFactory")
    void listen(String message, ConsumerRecordMetadata consumerRecordMetadata,
                @Headers Map<String, Object> headers
    )  {
        log.info("Received message\n---\nTOPIC: {}; PARTITION: {}; OFFSET: {};\nPAYLOAD: {}\n---",
                consumerRecordMetadata.topic(), consumerRecordMetadata.partition(), consumerRecordMetadata.offset(), message);
        //show kafka message headers
        headers.forEach((key, value) -> {
            if (value instanceof byte[] valueBytes)
                log.info(key + ": " + new String(valueBytes, StandardCharsets.UTF_8));
            else
                log.info(key + ": " + value);
        });
        //show current span info
        Span span = Span.current();
        log.info("kafka trace_id : " + span.getSpanContext().getTraceId());
        log.info("kafka span_id : " + span.getSpanContext().getSpanId());

        //send to next point
        String url = "http://localhost:"
                + port + "/path1";
        String response = restTemplate.getForObject(url, String.class);
        log.info("after  call to url: " + url);

    }

    public void send(String data) {

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, data);
        producerRecord
                .headers()
                .add("myheader", "myheadervalue".getBytes(StandardCharsets.UTF_8));
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        future.whenComplete((result, ex) -> {

            if (ex == null) {
                log.info("Sent message=[" + data + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            } else {
                log.error("Unable to send message=[" + data + "] due to : " + ex.getMessage());
            }
        });
    }

}
