package com.scy.register;

import com.alibaba.fastjson2.JSONArray;
import com.scy.comomon.RedisUtil;
import com.scy.comomon.URL;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class RedisRemoteRegist {
    public static void regist(String interfaceName, URL url) {
        Jedis jedis = RedisUtil.getJedis();
        // 先读取原来的
        String exist = jedis.get(interfaceName);

        List<URL> list = JSONArray.parseArray(exist, URL.class);

        if (list == null) {
            list = new ArrayList<>();
        }
        if (!list.contains(url))
            list.add(url);

        jedis.set(interfaceName, JSONArray.toJSONString(list));
    }

    public static List<URL> get(String interfaceName) {
        Jedis jedis = RedisUtil.getJedis();

        String json = jedis.get(interfaceName);

        List<URL> list = JSONArray.parseArray(json, URL.class);
        return list;
    }
}
