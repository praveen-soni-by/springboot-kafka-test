package com.syscho.kafka;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DummyTest {


    @BeforeEach
    void setup(){
        System.out.println("set up called");
    }


    @Test
    void testss(){

    }


    @Test
    void tests(){

    }

    @Test
    void test(){

    }

}
