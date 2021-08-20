/*
package com.syscho.kafka;

import com.syscho.kafka.producer.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final EventPublisher eventPublisher;

    @KafkaListener(topics = "order")
    public void placeOrder(String orderId) {
        log.info("received order with id : {} ", orderId);
        eventPublisher.send("payment", "Request for payment with order id " + orderId);
        log.info("Payment Request sent: {} ", orderId);
    }

    @KafkaListener(topics = "payment-completed")
    public void handlePaymentResponse(String payload) {
        log.info("payment done successfully : payload : {} ", payload);
        eventPublisher.send("shipment", "Request for payment with order id " + payload);
    }

    @KafkaListener(topics = "tracking")
    public void handleShipmentResponse(String trackingNumber) throws InterruptedException {
        log.info("Shipment in progress : trackingNumber number {} ", trackingNumber);
        Thread.sleep(1000);
        log.info("Checking current status for trackingNumber number {}",trackingNumber);
        eventPublisher.send("status", trackingNumber);
    }

    @KafkaListener(topics = "status-update")
    public void statusUpdate(@Payload int status) throws InterruptedException {
        if(status >7){
            eventPublisher.send("delivered", "delivered successfully");

        }else{
            Thread.sleep(1000);
            log.info("Checking current status for trackingNumber number");
            eventPublisher.send("status", trackingNumber);

        }
    }
}
*/
