package com.scy.proxy;

import com.scy.comomon.Invocation;
import com.scy.comomon.URL;
import com.scy.loadbalance.LoadBalance;
import com.scy.protocol.HttpClient;
import com.scy.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClazz) {
        // 用户配置

        Object proxy = Proxy.newProxyInstance(interfaceClazz.getClassLoader(), new Class[]{interfaceClazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(interfaceClazz.getName(), method.getName(),
                        method.getParameterTypes(), args);

                HttpClient httpClient = new HttpClient();
                // 服务发现
                List<URL> list = MapRemoteRegister.get(interfaceClazz.getName());
                // TODO: 负载均衡
                URL url = LoadBalance.random(list);
                return httpClient.send(url.getHost(), url.getPort(), invocation);
            }
        });
        return (T) proxy;
    }
}
