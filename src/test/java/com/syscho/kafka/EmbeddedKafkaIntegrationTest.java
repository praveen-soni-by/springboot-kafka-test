package com.syscho.kafka;

import com.syscho.kafka.consumer.EventConsumer;
import com.syscho.kafka.producer.EventPublisher;
import lombok.val;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.waitAtMost;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
class EmbeddedKafkaIntegrationTest {

    @Autowired
    private EventConsumer eventConsumer;

    @Autowired
    private EventPublisher producer;

    @Value("${topic.names.test-topic}")
    private String topic;

    @Autowired
    DefaultKafkaConsumerFactory defaultKafkaConsumerFactory;
    Consumer consumer;

    @BeforeEach
    void tets() {
        consumer = defaultKafkaConsumerFactory.createConsumer();
        consumer.subscribe(Collections.singleton("user-api"));

    }

    void  yes(){

        while (true) {
            ConsumerRecords<String,String> records = consumer.poll(10000);

              if(records.isEmpty())
                break;

            records.forEach(val -> System.out.println(val.topic()));
            System.out.println("---------------------------------------------------------");
            System.out.println("record" + records);

        }
    }

    @Test
    public void testShouldPublishEventAndConsume() {
        producer.send(topic, "Hello World");
  yes();
        waitAtMost(40000, TimeUnit.SECONDS)
                .untilAsserted(() -> {


                });

    }

   /* @KafkaListener(topics = "user-api")
    public void userRequest(String test) {
        if (test.contains("PROCESSED")) {
            producer.send("APPLICATION", test);
        } else {
            producer.send("REGISTRATION", test);
        }

    }*/
}