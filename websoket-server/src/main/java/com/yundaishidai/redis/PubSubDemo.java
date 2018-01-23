package com.yundaishidai.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class PubSubDemo 
{
    public static void main( String[] args )
    {
        // 替换成你的reids地址和端口
        String redisIp = "192.168.229.154";
        int reidsPort = 6379;
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisIp, reidsPort);

//        SubThread subThread = new SubThread(jedisPool);
//        subThread.start();

//        Publisher publisher = new Publisher(jedisPool);
//        publisher.publish("channel", "message");
    }
}