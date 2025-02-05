package com.scy.comomon;

import redis.clients.jedis.Jedis;

public class RedisUtil {
    private static final String REDIS_HOST = "47.94.233.70";
    private static final int REDIS_PORT = 6379;
    private static final String PASSWORD = "123456";

    private RedisUtil() {
    }

    private volatile static Jedis jedis;
    private volatile static Jedis jedis0;

    public static Jedis getJedis() {
        if (jedis == null) {
            synchronized (Jedis.class) {
                if (jedis == null) {
                    jedis = new Jedis(REDIS_HOST, REDIS_PORT);
                    jedis.auth(PASSWORD);
                    jedis.select(1);
                }
            }
        }
        return jedis;
    }

    public static Jedis getJedis0() {
        if (jedis0 == null) {
            synchronized (Jedis.class) {
                if (jedis0 == null) {
                    jedis0 = new Jedis(REDIS_HOST, REDIS_PORT);
                    jedis0.auth(PASSWORD);
                    jedis0.select(0);
                }
            }
        }
        return jedis0;
    }
}
