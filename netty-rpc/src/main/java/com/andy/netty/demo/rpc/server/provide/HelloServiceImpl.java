package com.andy.netty.demo.rpc.server.provide;

import com.andy.netty.demo.rpc.api.IHelloService;

/**
 * <p></p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/23 0023 21:13
 */
public class HelloServiceImpl implements IHelloService {

    @Override
    public String sayHello(String name) {
        return "hello: " + name;
    }
}
