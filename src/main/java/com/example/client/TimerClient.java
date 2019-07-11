package com.example.client;
 
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class TimerClient {
    private             TimerClientHandler timerClientHandler =  new TimerClientHandler();
    public void start() throws InterruptedException {
        Thread.sleep(5*1000);
        int port = 56789;
        try {
            new TimerClient().connect(port, "localhost");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//                            socketChannel.pipeline().addLast("decoder", new StringDecoder());
//                            socketChannel.pipeline().addLast("encoder", new StringEncoder());
                            socketChannel.pipeline().addLast("handler", timerClientHandler);
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            while(true){
                timerClientHandler.getFirstMessage().writeBytes(( "cd\r\n").getBytes());
//                if ("over".equals(in.readLine()) ){
//                    break;
//                }
//            }
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
 
    public static void main(String[] args) {
        int port = 56789;
        try {
            new TimerClient().connect(port, "localhost");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void sendMessage(String msg) {
        String body = msg+ "\r\n";
        timerClientHandler.getFirstMessage().writeBytes(body.getBytes());
    }
}