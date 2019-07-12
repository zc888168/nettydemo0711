package com.example.client;
 
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Service;

@Service
public class DemoClient {
    private DemoClientHandler timerClientHandler =  new DemoClientHandler();
    private Channel channel;
    public void start() throws InterruptedException {
        Thread.sleep(5*1000);
        int port = 56789;
        try {
            new DemoClient().connect(port, "localhost");
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
                            socketChannel.pipeline().addLast("handler", timerClientHandler);
                        }
                    });
             channel = b.connect(host, port).awaitUninterruptibly().sync().channel();

        } catch (Exception e){
            e.printStackTrace();
            group.shutdownGracefully();
        }
    }
 
    public static void main(String[] args) {
        int port = 56789;
        try {
            DemoClient timerClient =  new DemoClient();
            timerClient.connect(port, "localhost");
            Channel channel = timerClient.getChannel();
            String coutent = "this is just a test";
            ChannelFuture channelFuture = timerClient.writeMsg(channel, coutent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //todo
        }
    }

    private ChannelFuture writeMsg(Channel channel, String content) {
        if(null == channel || !channel.isActive()){
            System.out.println("发送消息失败，连接未注册或非活动状态[channel=" + channel + "]: content" + content);
        }
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer((content + "\r\n").getBytes()));


        return future;
    }


    public Channel getChannel() {
        return channel;
    }
}