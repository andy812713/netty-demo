package com.andy.netty.demo.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p></p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/23 0023 10:27
 */
@Slf4j
public class AioServer {

    private ExecutorService executorService;
    private AsynchronousChannelGroup channelGroup;
    public AsynchronousServerSocketChannel channel;

    public void start(int port) throws IOException, InterruptedException {
        // 1.创建一个缓存池
        executorService = Executors.newCachedThreadPool();
        // 2.创建通道组
        channelGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
        // 3.创建服务器通道
        channel = AsynchronousServerSocketChannel.open(channelGroup);
        // 4.进行绑定
        channel.bind(new InetSocketAddress(port));
        log.info("server start , port : " + port);
        channel.accept(this, new AIOServerHandler());
        Thread.sleep(Integer.MAX_VALUE);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AioServer server = new AioServer();
        server.start(9999);
    }
}
