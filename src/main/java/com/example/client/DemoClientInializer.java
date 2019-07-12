package com.example.client;

import com.example.protocol.MessageProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class DemoClientInializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder",
                new ProtobufDecoder(MessageProto.Message.getDefaultInstance()));
        ch.pipeline().addLast("encoder",
                new ProtobufEncoder());
        ch.pipeline().addLast("handler", new DemoClientHandler());
    }
}
