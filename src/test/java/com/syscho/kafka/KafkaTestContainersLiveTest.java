package com.syscho.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootKafkaApplication.class)
@DirtiesContext

public class KafkaTestContainersLiveTest {

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private DefaultKafkaConsumerFactory defaultKafkaConsumerFactory;
    private Consumer consumer;

    @org.junit.jupiter.api.Test
    void sendAndReceiveTest() throws InterruptedException {

        System.out.println("***************" + kafka.getBootstrapServers());

        Assertions.assertNotNull(kafkaTemplate);
        kafkaTemplate.send("bot", "Hey, I'm Alexa");
        listners();

    }

    @BeforeEach
    void tets() {
       /* kafka.start();
        consumer = defaultKafkaConsumerFactory.createConsumer();
        consumer.subscribe(Collections.singleton("bot"));
*/
    }

    void listners() {

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(10000);

            if (records.isEmpty()) {
                break;
            }
            ;
            for (ConsumerRecord rec : records) {
                System.out.println("RedisTestContainerTest.listners()");
                System.out.println(rec);
            }
        }

    }

}