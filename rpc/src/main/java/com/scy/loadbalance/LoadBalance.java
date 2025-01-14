package com.scy.loadbalance;

import com.scy.comomon.URL;

import java.util.List;
import java.util.Random;

/**
 * Todo: 查看负载均衡算法具体是如何实现的
 */
public class LoadBalance {
    public static URL random(List<URL> urls) {
        Random random = new Random();
        int i = random.nextInt(urls.size());
        return urls.get(i);
    }
}
