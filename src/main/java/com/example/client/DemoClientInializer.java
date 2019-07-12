package com.example.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class DemoClientInializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("handler", new DemoClientHandler());
    }
}
