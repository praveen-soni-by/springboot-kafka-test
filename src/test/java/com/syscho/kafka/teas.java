package com.syscho.kafka;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class teas {
    public static void main(String[] args) {

        System.out.println(DateTimeFormatter.ofPattern("HH:mm").format(LocalTime.MIN));
    }
}
