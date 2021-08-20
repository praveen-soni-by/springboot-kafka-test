package com.syscho.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Event {
    ORDER("order"), PAYMENT("payment"),
    SHIPMENT("shipment"),

    ORDER_COMPLETED("order-completed"),
    PAYMENT_DONE("payment-completed"),
    TRACKING("tracking"),
    DELIVERED("delivered");

    private final String value;

    Event(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Event fromValue(String value) {
        Event result = null;
        for (Event b : Event.values()) {
            if (b.value.equals(value)) {
                result = b;
            }
        }
        return result;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
