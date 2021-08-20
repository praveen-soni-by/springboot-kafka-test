package com.syscho.kafka.consumer;

import com.syscho.kafka.producer.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class GuessNumber {

    @Autowired
    private EventPublisher eventPublisher;

    @KafkaListener(topics = "guess-number")
    public void receive(String payload) {
        eventPublisher.send("choose-number", String.valueOf(new
                Random().nextInt(10 - 0 + 1) + 0));
    }
}
