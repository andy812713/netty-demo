package com.andy.netty.demo.tomcat.servlet;

import com.andy.netty.demo.tomcat.http.NettyRequest;
import com.andy.netty.demo.tomcat.http.NettyResponse;
import com.andy.netty.demo.tomcat.http.NettyServlet;

/**
 * <p></p>
 *
 * @author AndyWang QQ:295268319
 * @date 2019/6/23 0023 22:03
 */
public class FirstServlet extends NettyServlet {

    public void doGet(NettyRequest request, NettyResponse response) throws Exception {
        this.doPost(request, response);
    }

    public void doPost(NettyRequest request, NettyResponse response) throws Exception {
        response.write("This is First Serlvet");
    }

}
