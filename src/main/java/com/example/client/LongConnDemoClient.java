package com.example.client;

import com.example.config.DemoServerInfo;
import com.example.protocol.MessageProto;
import com.google.protobuf.ByteString;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LongConnDemoClient {
    private DemoClient demoClient;
    private DemoServerInfo demoServerInfo = new DemoServerInfo();
    private static volatile LongConnDemoClient longConnDemoClient = new LongConnDemoClient();

    private LongConnDemoClient() {
        init();

    }

    public static LongConnDemoClient getInstance() {
        return longConnDemoClient;
    }

    public void writeMsg(String content) {
        if(StringUtils.isEmpty(content)){
            return;
        }
        ByteString byteString = ByteString.copyFrom(content.getBytes());
        MessageProto.Message message = MessageProto.Message.newBuilder()
                .setId(content).setContent(2).setData(byteString).build();
        demoClient.writeMsg(demoClient.getChannel(), message);
    }

    private void init() {
        demoClient = new DemoClient();

        int port = demoServerInfo.getPort();
        try {
            demoClient.connect(port, demoServerInfo.getHost());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //定时任务 心跳检测 保持长连接
        keepAlive(demoClient);
    }

    private void keepAlive(DemoClient demoClient) {
        if (null == demoClient) {
            return;
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                doKeepAlive(demoClient);
            }


        }, new Date(), 5000L * 2);
    }

    private void doKeepAlive(DemoClient demoClient) {
        if(null == demoClient){
            return;
        }
        if(null == demoClient.getChannel()||!demoClient.getChannel().isActive()){
            connectServer(demoClient);
        }else{
            heartBeatServer(demoClient);
        }
    }

    private void heartBeatServer(DemoClient demoClient) {
        if(null == demoClient){
            return;
        }
        demoClient.ping();
    }

    private void connectServer(DemoClient demoClient) {
        if(null == demoClient){
            return;
        }
        try {
            demoClient.connect(demoServerInfo.getPort(), demoServerInfo.getHost());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
