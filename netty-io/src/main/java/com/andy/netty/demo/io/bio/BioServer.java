package com.andy.netty.demo.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>BIO</p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/22 0022 18:41
 */
@Slf4j
public class BioServer {

    private ServerSocket serverSocket;

    public BioServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log.info("服务端已启动, 端口号：{}", port);

    }

    public void listen() throws IOException {
        while (true){
            Socket acceptClient = serverSocket.accept();
            InputStream inputStream = acceptClient.getInputStream();
            byte [] buff = new byte[1024];
            int length = inputStream.read(buff);
            if(length > 0){
                String message = new String(buff,0,length);
                log.info("服务端收到消息：{}", message);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new BioServer(9999).listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
