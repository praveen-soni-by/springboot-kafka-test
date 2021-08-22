package com.syscho.kafka;

import static org.awaitility.Awaitility.waitAtMost;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.syscho.kafka.producer.EventPublisher;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9093" })
class EmbeddedKafkaIntegrationTest {

	@Autowired
	private EventPublisher producer;

	@Value("${topic.names.test-topic}")
	private String topic;

	@Autowired
	private DefaultKafkaConsumerFactory defaultKafkaConsumerFactory;
	private Consumer consumer;

	@BeforeEach
	void tets() {
		consumer = defaultKafkaConsumerFactory.createConsumer();
		consumer.subscribe(Collections.singleton("user-api"));

	}

	void listner() {

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(10000);

			if (records.isEmpty())
				break;

			records.forEach(val -> System.out.println(val.topic()));
			System.out.println("---------------------------------------------------------");
			System.out.println("record" + records);

		}
	}

	@Test
	public void testShouldPublishEventAndConsume() {
		producer.send(topic, "Hello World");
		listner();
		waitAtMost(40000, TimeUnit.SECONDS).untilAsserted(() -> {
			// assert
		});

	}

}