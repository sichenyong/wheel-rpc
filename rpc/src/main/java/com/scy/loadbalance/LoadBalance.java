package com.scy.loadbalance;

import com.scy.comomon.URL;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Todo: 查看负载均衡算法具体是如何实现的
 */
public class LoadBalance {
    public static AtomicInteger index = new AtomicInteger(0);

    /**
     * 随机策略
     * @param urls
     * @return
     */
    public static URL random(List<URL> urls) {
        Random random = new Random();
        int i = random.nextInt(urls.size());
        return urls.get(i);
    }

    /**
     * 轮询算法
     * @param urls
     * @return
     */
    public static URL RoundRobin(List<URL> urls) {
        int size = urls.size();
        int i = index.getAndIncrement() % size;
        return urls.get(i);
    }

    /**
     * 源ip 哈希算法
     * @param urls
     * @param ip
     * @return
     */
    public static URL IpHash(List<URL> urls, String ip) {
        int size = urls.size();
        int i = ip.hashCode() % size;
        return urls.get(i);
    }
}
