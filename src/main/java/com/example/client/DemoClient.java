package com.example.client;

import com.example.config.PingPong;
import com.example.protocol.MessageProto;
import com.google.protobuf.ByteString;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DemoClient {
    private Channel channel;



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


    public ChannelFuture writeMsg(Channel channel, MessageProto.Message message) {
        if (null == channel || !channel.isActive()) {
            System.out.println("发送消息失败，连接未注册或非活动状态[channel=" + channel + "]: content " + message.toString());
        }
        ChannelFuture future = channel.writeAndFlush(message);
        return future;
    }


    public Channel getChannel() {
        return channel;
    }

    public void ping( ){
        ByteString byteString = ByteString.copyFrom(PingPong.PING.getBytes());
        MessageProto.Message message = MessageProto.Message.newBuilder()
                .setId(PingPong.PING).setContent(0).setData(byteString).build();
        writeMsg(channel, message);
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