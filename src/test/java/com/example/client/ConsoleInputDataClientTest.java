package com.example.client;

import com.example.config.DemoServerInfo;
import io.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleInputDataClientTest {
    public static void main(String[] args) {
        DemoServerInfo demoServerInfo = new DemoServerInfo();
        int port = demoServerInfo.getPort();
        try {
            DemoClient demoClient = new DemoClient();
            demoClient.connect(port, demoServerInfo.getHost());
            Channel channel = demoClient.getChannel();
            String coutent = "please input data  by the console";

            demoClient.writeMsg(channel, coutent);
            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String data = in.readLine();
                demoClient.writeMsg(channel, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
