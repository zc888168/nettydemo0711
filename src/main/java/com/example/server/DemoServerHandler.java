package com.example.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
 
 
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
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("The time server receive order:" + body);
        String currentTime  = String.valueOf(new Date().getTime());
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
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