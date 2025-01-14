package com.scy.protocol;

import com.scy.comomon.Invocation;
import com.scy.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 实际处理请求部分
 */
public class HttpServerHandler {
    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        // 处理请求-->接口 方法 参数

        // 反序列化
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            // 获取实现类的类文件
            String interfaceName = invocation.getInterfaceName();
            Class clazz = LocalRegister.get(interfaceName, "1.0");

            Method method = clazz.getMethod(invocation.getMethodName(), invocation.getParameterTypes());

            // 调用方法
            String result = (String) method.invoke(clazz.getDeclaredConstructor().newInstance(), invocation.getParameters());

            // 响应
            IOUtils.write(result.getBytes(), resp.getOutputStream());

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
