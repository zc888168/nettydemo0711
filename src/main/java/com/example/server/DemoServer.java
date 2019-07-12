package com.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DemoServer {
    private static volatile  DemoServer instance = new DemoServer();
    private DemoServer(){

    }
    public static DemoServer getInstance(){
        return instance;
    }

    public void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new DemoServerInitializer());

            //绑定端口，开始接收进来的连接
            ChannelFuture channelFuture = b.bind(port).sync();
            new ShutdownHook("Thread-ConnletServer-Shutdown-Hook", channelFuture, bossGroup, workerGroup).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ShutdownHook extends Thread {
        private ChannelFuture channelFuture;
        private EventLoopGroup bossGroup;
        private  EventLoopGroup workerGroup;

        ShutdownHook(String name, ChannelFuture channelFuture, EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
            super(name);
            this.channelFuture = channelFuture;
            this.bossGroup =bossGroup;
            this.workerGroup = workerGroup;
        }

        public void run() {

            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
            System.out.println("客户端连接服务关闭成功");

        }
    }
}