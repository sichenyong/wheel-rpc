package com.scy;

import com.scy.comomon.URL;
import com.scy.protocol.HttpServer;
import com.scy.register.LocalRegister;
import com.scy.register.MapRemoteRegister;

public class Provider {
    public static void main(String[] args) {
        // 将实现类注册进去
        LocalRegister.regist(HelloService.class.getName(), "1.0", HelloServiceImpl.class);
        // 注册中心注册
        URL url = new URL("localhost", 8000);
        MapRemoteRegister.regist(HelloService.class.getName(), url);
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHost(), url.getPort());
    }
}
