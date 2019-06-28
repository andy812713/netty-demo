package com.andy.netty.demo.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Random;

/**
 * <p></p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/23 0023 20:38
 */
@Slf4j
public class AIOClient implements Runnable{

    private static Integer DEFAULT_PORT = 9999;
    private static String DEFAULT_ADDRESS = "127.0.0.1";
    private AsynchronousSocketChannel asynSocketChannel;

    public AIOClient() throws Exception {
        asynSocketChannel = AsynchronousSocketChannel.open();  // 打开通道
    }

    public void connect(){
        asynSocketChannel.connect(new InetSocketAddress(DEFAULT_ADDRESS, DEFAULT_PORT));  // 创建连接 和NIO一样
    }

    @Override
    public void run() {
        while(true){
        }
    }

    public void write(String request){
        try {
            asynSocketChannel.write(ByteBuffer.wrap(request.getBytes())).get();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            asynSocketChannel.read(byteBuffer).get();
            byteBuffer.flip();
            byte[] respByte = new byte[byteBuffer.remaining()];
            byteBuffer.get(respByte); // 将缓冲区的数据放入到 byte数组中
            log.info("客户端发送的消息：{}",new String(respByte,"utf-8").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            AIOClient myClient = new AIOClient();
            myClient.connect();
            new Thread(myClient, "myClient").start();
            String []operators = {"+","-","*","/"};
            Random random = new Random(System.currentTimeMillis());
            String expression = random.nextInt(10)+operators[random.nextInt(4)]+(random.nextInt(10)+1);
            myClient.write(expression);
        }
    }
}
