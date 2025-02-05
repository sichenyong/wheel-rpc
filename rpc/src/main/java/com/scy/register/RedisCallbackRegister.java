package com.scy.register;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.scy.comomon.RedisUtil;
import redis.clients.jedis.Jedis;

public class RedisCallbackRegister {
    public static void regist(String interfaceName, String version, Class clazz) {
        Jedis jedis = RedisUtil.getJedis0();

        jedis.set(interfaceName + version, JSON.toJSONString(clazz.getName()));
    }

    public static String get(String interfaceName, String version) {
        Jedis jedis = RedisUtil.getJedis0();

        String result = jedis.get(interfaceName + version);

       return JSON.parseObject(result, String.class);
    }
}
