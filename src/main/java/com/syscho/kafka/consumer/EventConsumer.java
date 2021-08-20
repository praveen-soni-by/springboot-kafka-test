package com.syscho.kafka.consumer;

import com.syscho.kafka.producer.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventConsumer {

    private String payload = null;
    @Autowired
    private
    EventPublisher eventPublisher;

    @KafkaListener(topics = "${topic.names.test-topic}")
    public void receive(String payload) {
        setPayload(payload);
        System.out.println("processing done  , sending event to user api");
        System.out.println("*********************************************");
        eventPublisher.send("user-api", "Registration in process");
    }

    @KafkaListener(topics = "REGISTRATION")
    public void registration(String payload) {
        System.out.println("Registration Done");
    }

    @KafkaListener(topics = "APPLICATION")
    public void application(String payload) {
        System.out.println("APPLICATION Done");

    }

    @KafkaListener(topics = "${topic.names.otp}")
    public void receiveOTP(String otp) {
        log.info("received OTP :'{}'", otp);
    }


    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
