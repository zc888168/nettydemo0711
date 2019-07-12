package com.example.client;

import com.example.config.DemoServerInfo;
import com.example.protocol.MessageProto;
import com.google.protobuf.ByteString;
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
            String content = "please input data  by the console";
            ByteString byteString = ByteString.copyFrom(content.getBytes());
            MessageProto.Message message = MessageProto.Message.newBuilder().setId(content)
                    .setContent(1).setData(byteString).build();

            demoClient.writeMsg(channel, message);
            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String data = in.readLine();
                 byteString = ByteString.copyFrom(data.getBytes());
                 message = MessageProto.Message.newBuilder().setId(data)
                        .setContent(1).setData(byteString).build();
                demoClient.writeMsg(channel, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
