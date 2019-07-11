package com.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import org.springframework.stereotype.Service;

@Service
public class TimerServer {
    public void start(){
        int port = 56789;
        try {
            new TimerServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
 
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
 
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
 
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
 
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//            socketChannel.pipeline().addLast("decoder", new StringDecoder());
//            socketChannel.pipeline().addLast("encoder", new StringEncoder());
            socketChannel.pipeline().addLast("handler", new TimeServerHandler());
        }
    }
 
    public static void main(String[] args) {
        int port = 56789;
        try {
            new TimerServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}