package com.blueyonder.exec.ecom.trans.planning.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

class MockDpProducerIntegrationTest extends MockKafkaConfig {

    private final String TRACKING_INITIATE_TRACKING = "trans.commands.initiate-tracking.v1";
    private final String TRACKING_CANCEL_TRACKING = "trans.commands.cancel-tracking.v1";

    @Test
    void testShouldPublish_InitiateTrackingEvent() {

        String payload = "{\n"
                + "  \"payload\": {\n"
                + "    \"entityId\": \"transportId\",\n"
                + "    \"entityType\": \"transportInstruction\"\n"
                + "  },\n"
                + "  \"eventName\": \"initiateTracking\"\n"
                + "}";

        producer.send(new ProducerRecord<>("trans.commands.planning-initiate-tracking.v1", payload.getBytes()));

        Awaitility.await().untilAsserted(() -> {
            ConsumerRecord<String, byte[]> received = consumerRecords.poll(2, TimeUnit.SECONDS);
            ObjectMapper objectMapper = new ObjectMapper();

            ObjectNode data = objectMapper.readValue(received.value(), ObjectNode.class);
            Assertions.assertEquals(TRACKING_INITIATE_TRACKING, received.topic());
            Assertions.assertEquals("CREATE", data.get("action").textValue());
            Assertions.assertEquals("0174c671-8b39-42ea-adf2-1e020b5fadd7", data.get("shipmentId").textValue());
            Assertions.assertEquals("BYCARRIER", data.get("carrierName").textValue());
        });
    }

    @Test
    void testShouldPublish_CancelledTracking() throws IOException, InterruptedException {
        String payload = "{\n"
                + "  \"payload\": {\n"
                + "    \"entityId\": \"transportId\",\n"
                + "    \"entityType\": \"transportInstruction\"\n"
                + "  },\n"
                + "  \"eventName\": \"cancelTracking\"\n"
                + "}";

        producer.send(new ProducerRecord<>("trans.commands.planning-cancel-tracking.v1", payload.getBytes()));

        ObjectMapper objectMapper = new ObjectMapper();
        ConsumerRecord<String, byte[]> received = consumerRecords.poll(2, TimeUnit.SECONDS);

        ObjectNode data = objectMapper.readValue(received.value(), ObjectNode.class);
        Assertions.assertEquals("CANCELLED", data.get("action").textValue());
        Assertions.assertEquals(TRACKING_CANCEL_TRACKING, received.topic());
        Assertions.assertEquals("0174c671-8b39-42ea-adf2-1e020b5fadd7", data.get("shipmentId").textValue());
        Assertions.assertEquals("BYCARRIER", data.get("carrierName").textValue());
    }

    @Test
    void testShouldNotPublishAnyEvent_WhenPartnerInfoIsNull() throws InterruptedException {
        String payload = "{\n"
                + "  \"payload\": {\n"
                + "    \"entityId\": \"transportId_without_partner\",\n"
                + "    \"entityType\": \"transportInstruction\"\n"
                + "  },\n"
                + "  \"eventName\": \"initiateTracking\"\n"
                + "}";

        producer.send(new ProducerRecord<>("trans.commands.planning-initiate-tracking.v1", payload.getBytes()));

        ConsumerRecord<String, byte[]> received = consumerRecords.poll(2, TimeUnit.SECONDS);
        Assertions.assertNull(received);
    }

    @Override
    protected String[] buildConsumeTopic() {
        List<String> topics = new ArrayList<>();
        topics.add(TRACKING_INITIATE_TRACKING);
        topics.add(TRACKING_CANCEL_TRACKING);
        return topics.toArray(new String[] {});
    }
}