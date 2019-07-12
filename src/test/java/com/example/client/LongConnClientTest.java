package com.example.client;

public class LongConnClientTest {
    public static void main(String[] args) {
        LongConnDemoClient longConnDemoClient = LongConnDemoClient.getInstance();
        longConnDemoClient.writeMsg("i am mike");
    }
}
