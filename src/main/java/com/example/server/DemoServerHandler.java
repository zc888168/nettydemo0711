package com.example.server;

import com.example.config.PingPong;
import com.example.protocol.MessageProto;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
 
 
public class DemoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送 过来的数据  进行相关业务处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server receive msg ....");
        MessageProto.Message message = (MessageProto.Message) msg;

        String body = message.getId();
        System.out.println("The time server receive order:" + body);
        MessageProto.Message revData;
        if ((PingPong.PING ).equals(body)){
            ByteString byteString = ByteString.copyFrom(PingPong.PONG.getBytes());
            revData = MessageProto.Message.newBuilder().setId(PingPong.PONG)
                    .setContent(2).setData(byteString).build();
        } else {
            ByteString byteString = ByteString.copyFrom(body.getBytes());
            revData = MessageProto.Message.newBuilder().setId(body)
                    .setContent(2).setData(byteString).build();
        }

        ctx.writeAndFlush(revData);
        System.out.println("server send msg ....");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        /**
         * 客户度已经链接上服务端 这里面可以添加到连接池 但是未登录 后面可以进行登录操作
         */
        System.out.println("有新的客户端连接到DemoServer " + incoming.remoteAddress() +"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("客户端断开与DemoServer的连接 client: " + incoming.remoteAddress());
    }
}