package com.andy.netty.demo.io.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
public class NIOServer {
    private int port = 9999;

    private Selector selector;
    //缓冲区 Buffer 等候区
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public NIOServer(int port) throws IOException {
        this.port = port;
        // 打开服务器套接字通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(this.port));
        //采用非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 打开一个选择器
        selector = Selector.open();

        // 服务器套接字注册到Selector中 并指定Selector监控连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void listen() throws IOException {
        while(true){
            // 没有通道就绪 一直阻塞 返回已经就绪通道的数目(有可能为0)
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
        if(key.isAcceptable()){// 是否有连接进来
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel channel = server.accept();
            //设置为非阻塞
            channel.configureBlocking(false);
            // SocketChannel通道的可读事件注册到Selector中
            key = channel.register(selector,SelectionKey.OP_READ);

            // 连接成功 向Client打个招呼
            if (channel.isConnected()) {
                buffer.clear();
                buffer.put("I am Server...".getBytes());
                buffer.flip();
                channel.write(buffer);
            }

        } else if(key.isReadable()){// 通道的可读事件就绪
            //多路复用
            SocketChannel channel = (SocketChannel) key.channel();
            buffer.clear(); // 清空缓冲区
            int length = channel.read(buffer);
            if(length > 0){
                buffer.flip();
                String content = new String(buffer.array(),0, length);

                // SocketChannel通道的可写事件注册到Selector中
                key = channel.register(selector, SelectionKey.OP_WRITE);
                key.attach(content);
                log.info("server读取的内容为：{},LocalAddress:{}", content, channel.getLocalAddress());
            }
        } else if(key.isWritable()){ // 通道的可写事件就绪
            SocketChannel channel = (SocketChannel) key.channel();

            buffer.clear(); // 清空缓冲区
            // 准备发送的数据
            String messageFromServer = "Hello,Client... " + channel.getLocalAddress();
            buffer.put(messageFromServer.getBytes());
            buffer.flip();

            channel.write(buffer);
            log.info("Server发送的数据:{}", messageFromServer);

//            String attachment = (String) key.attachment();
            //channel.write(ByteBuffer.wrap(("输出:" + attachment).getBytes()));

            key = channel.register(selector, SelectionKey.OP_READ);
            channel.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new NIOServer(9999).listen();
    }
}
