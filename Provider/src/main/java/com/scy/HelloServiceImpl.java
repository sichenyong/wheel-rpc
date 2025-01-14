package com.scy;

public class HelloServiceImpl implements HelloService{
    @Override
    public String SayHello(String msg) {
        return "hello: " + msg;
    }
}
