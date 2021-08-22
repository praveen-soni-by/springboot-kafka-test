package com.syscho.kafka;

import java.util.Collections;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
class KaftaTestContainerTest {

	@ClassRule
	public static KafkaContainer kafka = new

	KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))
	  .withExposedPorts(9092,9093,9094)
			.withEnv("KAFKA_LISTENERS", "PLAINTEXT://locahost:9092 , BROKER://localhost:9092").withExposedPorts(9092);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private DefaultKafkaConsumerFactory defaultKafkaConsumerFactory;
	private Consumer consumer;

	@org.junit.jupiter.api.Test
	void sendAndReceiveTest() throws InterruptedException {
		kafka.start();

		System.out.println("***************" + kafka.getBootstrapServers());

		Assertions.assertNotNull(kafkaTemplate);
		kafkaTemplate.send("bot", "Hey, I'm Alexa");
		listners();

	}

	@BeforeEach
	void tets() {
		consumer = defaultKafkaConsumerFactory.createConsumer();
		consumer.subscribe(Collections.singleton("bot"));

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