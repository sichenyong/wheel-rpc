package com.scy;

import com.scy.protocol.HttpServer;
import com.scy.register.LocalRegister;

public class Provider {
    public static void main(String[] args) {
        // 将实现类注册进去
        LocalRegister.regist(HelloService.class.getName(), "1.0", HelloServiceImpl.class);
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost", 8000);
    }
}
