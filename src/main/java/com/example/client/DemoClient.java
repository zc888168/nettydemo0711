package com.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class DemoClient {
    private Channel channel;
    private static final String PING = "ping";

    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new DemoClientInializer());
            channel = b.connect(host, port).awaitUninterruptibly().sync().channel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 56789;
        try {
            DemoClient demoClient = new DemoClient();
            demoClient.connect(port, "localhost");
            Channel channel = demoClient.getChannel();
            String coutent = "please intput data  by the console";

            demoClient.writeMsg(channel, coutent);
            while(true){
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String data = in.readLine();
                demoClient.writeMsg(channel,  data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ChannelFuture writeMsg(Channel channel, String content) {
        if (null == channel || !channel.isActive()) {
            System.out.println("发送消息失败，连接未注册或非活动状态[channel=" + channel + "]: content " + content);
        }
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer((content + "\r\n").getBytes()));
        return future;
    }


    public Channel getChannel() {
        return channel;
    }

    public void ping( DemoClient demoClient){
        demoClient.writeMsg(demoClient.getChannel(), PING);
    }
    public void shutdown(Channel channel, EventLoopGroup group) {
        if (channel != null && channel.isActive()) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    public boolean closed(Channel channel) {
        return channel == null ? true : !channel.isActive();
    }
}