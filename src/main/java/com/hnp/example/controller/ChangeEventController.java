package com.hnp.example.controller;

import com.hnp.example.event.ConfigChangeEvent;
import com.hnp.example.producer.ConfigChangeEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChangeEventController {

    private final ConfigChangeEventProducer configChangeEventProducer;

    @PostMapping(value = "/configs/events", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerChangeEvent(@RequestBody ConfigChangeEvent configChangeEvent) {
        try {
            this.configChangeEventProducer.onConfigChangeEvent(configChangeEvent);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Failed to produce the message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
