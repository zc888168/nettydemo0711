package com.example.server;

import com.example.config.DemoServerInfo;

public class ServerMain {

    public static void main(String[] args) {
        DemoServerInfo demoServerInfo = new DemoServerInfo();
        DemoServer demoServer = DemoServer.getInstance();
        try {
            demoServer.bind(demoServerInfo.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
