package com.hnp.example.producer;

import com.hnp.example.event.ConfigChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigChangeEventProducer {

    private final KafkaTemplate<String, ConfigChangeEvent> kafkaTemplate;

    @Value("${kafka.config.change.topic}")
    private String configChangeEventTopic;

    public void onConfigChangeEvent(ConfigChangeEvent configChangeEvent) {

        Message<ConfigChangeEvent> message = MessageBuilder
                .withPayload(configChangeEvent)
                .setHeader(KafkaHeaders.TOPIC, this.configChangeEventTopic)
                .build();

        ListenableFuture<SendResult<String, ConfigChangeEvent>> future = this.kafkaTemplate.send(message);

        future.addCallback(
                (result -> log.info("Successfully produced the config change event: {}", configChangeEvent)),
                (ex -> log.error("Failed to produce the config change event: {}", configChangeEvent))
        );
    }
}
