package com.scy;

import com.scy.callback.HelloServiceErrorCallback;
import com.scy.comomon.URL;
import com.scy.protocol.HttpServer;
import com.scy.register.*;

public class Provider {

    public static void main(String[] args) {
        // 实现类注册
        LocalRegister.regist(HelloService.class.getName(), "1.0", HelloServiceImpl.class);
        // 服务降级方法
//        LocalCallbackRegister.regist(HelloService.class.getName(), "1.0", HelloServiceErrorCallback.class);
        RedisCallbackRegister.regist(HelloService.class.getName(), "1.0", HelloServiceErrorCallback.class);
        URL url = new URL("localhost", 8000);
        // 服务注册
//        MapRemoteRegister.regist(HelloService.class.getName(), url);

        RedisRemoteRegist.regist(HelloService.class.getName(), url);
        HttpServer server = new HttpServer();
        server.start(url.getHost(), url.getPort());
    }
}
