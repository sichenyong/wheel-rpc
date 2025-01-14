package com.scy;

import com.scy.comomon.Invocation;
import com.scy.protocol.HttpClient;
import com.scy.proxy.ProxyFactory;

public class Consumer {
    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.SayHello("rpc666");
        System.out.println(result);
    }
}
