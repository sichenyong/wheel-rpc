package com.scy.protocol;

import com.alibaba.fastjson2.JSONArray;
import com.scy.comomon.RedisUtil;
import com.scy.comomon.URL;
import jdk.swing.interop.DispatcherWrapper;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;

public class HttpServer {
    public void start(String hostname, Integer port) {
        // 读取用户的配置 server.name= tomcat ? netty
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(port);

        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostname);

        Host host = new StandardHost();
        host.setName(hostname);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet());
        context.addServletMappingDecoded("/*", "dispatcher");

        //  服务器关闭，从注册中心移除
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Jedis jedis = RedisUtil.getJedis();
            ScanParams params = new ScanParams().count(5);
            URL url = new URL(hostname, port);
            String cursor = "0";
            do {
                ScanResult<String> scan = jedis.scan(cursor, params);
                cursor = scan.getCursor();
                for (String key : scan.getResult()) {
                    String urls = jedis.get(key);
                    List<URL> urlList = JSONArray.parseArray(urls, URL.class);
                    boolean remove = urlList.remove(url);
                    if (urlList.isEmpty()) {
                        // 此时这个接口没有服务器节点了，删除
                        System.out.println("[终止线程]删除KEY:\t" + key);
                        jedis.del(key);
                    } else {
                        System.out.println("[终止线程]修改KEY:\t" + key);
                        jedis.set(key, JSONArray.toJSONString(urlList));
                    }
                }
            } while (!cursor.equals("0"));
        }, "Shutdown-thread"));

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
