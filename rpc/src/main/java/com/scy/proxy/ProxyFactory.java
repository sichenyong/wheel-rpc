package com.scy.proxy;

import com.scy.comomon.Invocation;

import com.scy.comomon.URL;
import com.scy.loadbalance.LoadBalance;
import com.scy.protocol.HttpClient;
import com.scy.register.LocalCallbackRegister;
import com.scy.register.MapRemoteRegister;
import com.scy.register.RedisCallbackRegister;
import com.scy.register.RedisRemoteRegist;
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
//                List<URL> list = MapRemoteRegister.get(interfaceClazz.getName());
                List<URL> list = RedisRemoteRegist.get(interfaceClazz.getName());
                // 服务调用
                Object result = null;
                int maxIter = 1;
//                while (maxIter > 0) {
//
//                    // 演示重试
////                    Thread.sleep(2000);
//                }

                // TODO: 负载均衡
                URL url = LoadBalance.random(list);
                try {
                    result = httpClient.send(url.getHost(), url.getPort(), invocation);
                } catch (Exception e) {
                    // error-callback=com.scy.HelloServiceErrorCallback
                    if (-- maxIter == 0) {
                        // 调用服务降级方法
                        Class clazz = RedisCallbackRegister.get(interfaceClazz.getName(), "1.0");
                        Method method1 = clazz.getMethod(method.getName(), method.getParameterTypes());
                        result = method1.invoke(clazz.getConstructor().newInstance(), args);
                        return result;
                    }
                }

                return result;
            }
        });
        return (T) proxy;
    }
}
