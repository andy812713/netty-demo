package com.andy.netty.demo.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * <p></p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/23 0023 20:24
 */
@Slf4j
public class AIOServerHandler implements
        CompletionHandler<AsynchronousSocketChannel, AioServer> {


    @Override
    public void completed(AsynchronousSocketChannel asynSocketChannel, AioServer attachment) {
        // 保证多个客户端都可以阻塞
        attachment.channel.accept(attachment, this);
        read(asynSocketChannel);
    }



    @Override
    public void failed(Throwable exc, AioServer attachment) {
        log.info("IO操作异常：{}",exc);
    }


    private void read(final AsynchronousSocketChannel asynSocketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        asynSocketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer resultSize, ByteBuffer attachment) {
                attachment.flip();
                String resultData = new String(attachment.array()).trim();
                log.info("Server -> " + "收到客户端的数据信息为:{}", resultData + "==2222");
                try {
                    write(asynSocketChannel, resultData);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    private void write(AsynchronousSocketChannel asynSocketChannel, String response)
            throws ExecutionException, InterruptedException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(response.getBytes());
        buf.flip();
        // 在从缓冲区写入到通道中
        asynSocketChannel.write(buf).get();
    }
}
