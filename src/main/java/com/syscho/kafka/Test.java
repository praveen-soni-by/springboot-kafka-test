package com.syscho.kafka;

import lombok.Data;

@Data
class Stuent{
    private int num;
}
@Data
class Employee{
    Stuent stuent;
}

public class Test {
    public static void main(String[] args) {
        Employee employee = new Employee();

        if(employee.getStuent() == null){
            System.out.println("EEEEEEEEE");
        }

    }
}
