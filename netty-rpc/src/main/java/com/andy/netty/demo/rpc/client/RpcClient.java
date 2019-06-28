package com.andy.netty.demo.rpc.client;

import com.andy.netty.demo.rpc.api.IHelloService;
import com.andy.netty.demo.rpc.client.proxy.RpcProxy;
import lombok.extern.slf4j.Slf4j;

/**
 * <p></p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/23 0023 21:09
 */
@Slf4j
public class RpcClient {

    public static void main(String[] args) {
        IHelloService rpcHello = RpcProxy.create(IHelloService.class);
        log.info(rpcHello.sayHello("Andy"));
    }
}
