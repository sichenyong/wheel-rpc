package com.scy.callback;

import com.scy.HelloService;

public class HelloServiceErrorCallback implements HelloService {
    @Override
    public String SayHello(String msg) {
        return "下游服务出现异常！";
    }
}
