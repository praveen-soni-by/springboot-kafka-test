package com.syscho.kafka;

import java.util.Collections;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.syscho.kafka.producer.EventPublisher;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
class ChatAppTest {

	@Autowired
	private EventPublisher producer;

	@Autowired
	private DefaultKafkaConsumerFactory defaultKafkaConsumerFactory;
	private Consumer consumer;

	@BeforeEach
	void tets() {
		consumer = defaultKafkaConsumerFactory.createConsumer();
		consumer.subscribe(Collections.singleton("bot"));
		System.out.println("222222****************************************************************************");
	}

	void listners() {
		boolean won = false;
		int tryCount = 5;
		while (true && !won && tryCount > 0) {
			ConsumerRecords<String, String> records = consumer.poll(10000);

			if (records.isEmpty()) {
				break;
			}
			;
			for (ConsumerRecord rec : records) {
				System.out.println("Guess Number is " + rec.value());
				if (rec.topic().equalsIgnoreCase("bot")) {
					if (String.valueOf(rec.value()).equalsIgnoreCase("8")) {
						System.out.println("*************************************************");
						System.out.println("You guess the right number");
						System.out.println("*************************************************");
						won = true;
						break;
					} else {
						producer.send("user-chat", "8");
					}

				}
				--tryCount;
			}
		}
		if (!won) {
			System.out.println("---------------------------------------------");
			System.out.println("You such a looser");
			System.out.println("---------------------------------------------");

		}
	}

	@Test
	public void testShouldPublishEventAndConsumse() {
		producer.send("user-chat", "3");
		//listners();

	}
	@Test
	public void testShouldPublishEventAndConsume() {
		producer.send("user-chat", "3");
		listners();

	}
}