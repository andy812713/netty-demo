package com.andy.netty.demo.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <p>BIO客户端</p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/22 0022 19:25
 */
@Slf4j
public class BioClient {

    private final int DEFAULT_PORT = 9999;
    private final String DEFAULT_SERVER_IP = "127.0.0.1";

    public void send(){
        OutputStream outputStream = null;
        Socket socket = null;
        try {
            socket = new Socket(DEFAULT_SERVER_IP, DEFAULT_PORT);
            outputStream = socket.getOutputStream();
            String message = "I'm client";
            outputStream.write(message.getBytes());
            outputStream.flush();
            log.info("客户端发送的消息：{}",message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new BioClient().send();
    }
}
