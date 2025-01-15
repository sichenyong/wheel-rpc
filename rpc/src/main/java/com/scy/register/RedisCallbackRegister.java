package com.scy.register;

import com.scy.comomon.RedisUtil;
import redis.clients.jedis.Jedis;

public class RedisCallbackRegister {
    public static void regist(String interfaceName, String version, Class clazz) {
        Jedis jedis = RedisUtil.getJedis();

        jedis.set(interfaceName + version, clazz.getName());
    }

    public static Class get(String interfaceName, String version) throws ClassNotFoundException {
        Jedis jedis = RedisUtil.getJedis();

        String result = jedis.get(interfaceName + version);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.loadClass(result);
//        return Class.forName(result);
    }
}
