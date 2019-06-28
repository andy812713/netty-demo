package com.andy.netty.demo.io.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <p></p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/23 0023 8:53
 */
@Slf4j
public class NIOClient {

    private final int DEFAULT_PORT = 9999;
    private final String DEFAULT_SERVER_IP = "127.0.0.1";
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private Selector selector = null;
    public void init() throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞状态
        socketChannel.configureBlocking(false);
        // 打开选择器
        selector = Selector.open();
        //注册
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        //发起连接
        socketChannel.connect(new InetSocketAddress(DEFAULT_SERVER_IP, DEFAULT_PORT));
    }

    public void connect() throws IOException {
        while (true){
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()){
                // SelectionKey封装了一个通道和选择器的注册关系
                SelectionKey key = iter.next();
                // Selector不会移除SelectionKey 处理完了手动移除
                iter.remove();

                process(key);
            }
        }
    }

    private void process(SelectionKey key) throws IOException {
        if(key.isConnectable()){
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 完成连接
            if(socketChannel.isConnectionPending()){
                socketChannel.finishConnect();
                log.info("连接成功");

                // 发送数据给Server
                String messageToServer = "Hello,Server...";
                buffer.clear();
                buffer.put(messageToServer.getBytes());
                buffer.flip();
                socketChannel.write(buffer);
                log.info("client发送给服务器的信息：{}", messageToServer);
                key = socketChannel.register(selector, SelectionKey.OP_READ);
            } else {
                log.info("client connected fail");
                System.exit(1);
            }
        }else if(key.isReadable()){
            SocketChannel socketChannel = (SocketChannel) key.channel();
            buffer.clear();
            int length = socketChannel.read(buffer);
            if(length > 0){
                buffer.flip();
                String content = new String(buffer.array(),0, length);

                // SocketChannel通道的可写事件注册到Selector中
                key = socketChannel.register(selector, SelectionKey.OP_WRITE);
                key.attach(content);
                log.info("client读取的内容为：{}, LocalAddress:{}", content, socketChannel.getLocalAddress());
            }
        }else if(key.isWritable()){
            SocketChannel socketChannel = (SocketChannel) key.channel();
            buffer.clear();
            // 准备发送的数据
            String messageFromServer = "Hello,Server... " + socketChannel.getLocalAddress();
            buffer.put(messageFromServer.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            log.info("Client发送的数据:" + messageFromServer);
            key = socketChannel.register(selector, SelectionKey.OP_READ);
        }
    }

    public static void main(String[] args) throws IOException {

        NIOClient client = new NIOClient();
        client.init();
        client.connect();
    }
}
