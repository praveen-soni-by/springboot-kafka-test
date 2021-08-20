package com.syscho.kafka;

import com.syscho.kafka.consumer.EventConsumer;
import com.syscho.kafka.producer.EventPublisher;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.waitAtMost;

@SpringBootTest
@EmbeddedKafka(partitions = 4)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventCommunicaitonTest {

    private static final String TOPIC = "test-topic";

    @Test
    void testPartitions(@Autowired KafkaAdmin admin) throws InterruptedException, ExecutionException {
        AdminClient client = AdminClient.create(admin.getConfigurationProperties());

        Map<String, TopicDescription> map = client.describeTopics(Collections.singletonList("foo")).all().get();
        System.out.println(map.values().iterator().next().partitions().size());
    }

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    BlockingQueue<ConsumerRecord<String, String>> records;

    KafkaMessageListenerContainer<String, String> container;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer());
        ContainerProperties containerProperties = new ContainerProperties(TOPIC,"love");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, String>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }

    @Test
    public void kafkaSetup_withTopic_ensureSendMessageIsReceived() throws Exception {
        // Arrange
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();

        // Act
        producer.send(new ProducerRecord<>(TOPIC, "test-topic", "{\"event\":\"Test Event\"}"));
        producer.flush();
        // Assert
        waitAtMost(40000000, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    System.out.println("*************");
                    ConsumerRecord<String, String> singleRecord = records.poll(100, TimeUnit.MILLISECONDS);
                    Assertions.assertNotNull(singleRecord);
                    Assertions.assertEquals("test-topic", singleRecord.key());
                    Assertions.assertEquals("{\"event\":\"Test Event\"}", singleRecord.value());

                });


    }
}