package com.scy;

import com.alibaba.fastjson2.JSONArray;
import com.scy.comomon.RedisUtil;
import com.scy.comomon.URL;
import com.scy.register.MapRemoteRegister;
import org.apache.tomcat.util.buf.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 监听redis中服务节点的变化，修改本地缓存
 */
public class Subscribe {
    private static BlockingQueue<String> task = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        // 启动订阅线程
        new Thread(Subscribe::startSubscription).start();

        // 启动任务处理线程
        new Thread(Subscribe::processTask).start();

        // 主线程保持运行
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 订阅 Redis 事件
    private static void startSubscription() {
        Jedis jedis = new Jedis("47.94.233.70", 6379);
        jedis.auth("123456");
        jedis.select(1);

        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                String action = channel.split(":")[1];
                System.out.println("[订阅线程] 收到事件: " + action + ", Key: " + message);
                task.offer(action + "-" + message); // 非阻塞添加任务
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                System.out.println("[订阅线程] 成功订阅: " + channel);
            }
        }, "__keyevent@1__:set", "__keyevent@1__:del");

        // 此处代码不会执行到，除非订阅被取消
    }

    // 处理任务队列
    private static void processTask() {
        try (Jedis jedis = new Jedis("47.94.233.70", 6379)) {
            jedis.auth("123456");
            jedis.select(1);

            while (true) {
                String mission = task.take();
                System.out.println("[处理线程] 处理任务: " + mission);
                String[] parts = mission.split("-");
                String action = parts[0];
                String key = parts[1];

                if ("set".equalsIgnoreCase(action)) {
                    if(key.contains("1.0")) continue;
                    String value = jedis.get(key);
                    List<URL> list = JSONArray.parseArray(value, URL.class);
                    for (URL url : list) {
                        System.out.println("[处理线程] Key=" + key + ", 值=" + value + ", 注册进缓存");
                        MapRemoteRegister.regist(key, url);
                    }
                } else if ("del".equalsIgnoreCase(action)) {
                    System.out.println("[处理线程] 删除 Key: " + key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
