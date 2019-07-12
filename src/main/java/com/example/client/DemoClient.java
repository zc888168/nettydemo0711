package com.example.client;

import com.example.config.PingPong;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
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


    public ChannelFuture writeMsg(Channel channel, String content) {
        if (null == channel || !channel.isActive()) {
            System.out.println("发送消息失败，连接未注册或非活动状态[channel=" + channel + "]: content " + content);
        }
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer((content + "\r\n").getBytes()));
        return future;
    }


    public Channel getChannel() {
        return channel;
    }

    public void ping( ){
        writeMsg(channel, PingPong.PING);
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