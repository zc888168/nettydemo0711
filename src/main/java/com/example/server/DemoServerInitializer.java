package com.example.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class DemoServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("handler", new DemoServerHandler());
    }
}
