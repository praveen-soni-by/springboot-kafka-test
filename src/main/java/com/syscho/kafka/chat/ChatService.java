package com.syscho.kafka.chat;

import com.syscho.kafka.producer.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatService {

    private final EventPublisher eventPublisher;

    @KafkaListener(topics = "user-chat")
    public void receive(String payload) {
        int number = (int) (Math.random() * (8 - 0 + 1) + 0);

        eventPublisher.send("bot", String.valueOf(number));
    }

    @KafkaListener(topics = "bobby-chat")
    public void chat2(String payload) {
        int number = (int) (Math.random() * (8 - 0 + 1) + 0);

        eventPublisher.send("bot", "Hi Bobby Here");
    }

    @KafkaListener(topics = "lucky-chat")
    public void chat3(String payload) {
        int number = (int) (Math.random() * (8 - 0 + 1) + 0);
        eventPublisher.send("bot", "Hi Lucky Here Here");
    }

}
