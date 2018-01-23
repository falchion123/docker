package com.yundaishidai.redis.pub;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Publisher {
	
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	
    private final JedisPool jedisPool;

    public Publisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    
    /** 
     * 推入消息到redis消息通道 
     *  
     * @param String 
     *            channel 
     * @param String 
     *            message 
     */  
    public  void publish(String channel, String message) {  
        Jedis jedis = null;  
        try {  
            jedis  = jedisPool.getResource();
            jedis.publish(channel, message);  
        } catch (Exception e) {
        	logger.error("publish channel error:", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }  
  
    /** 
     * 推入消息到redis消息通道 
     *  
     * @param byte[] 
     *            channel 
     * @param byte[] 
     *            message 
     */  
    public void publish(byte[] channel, byte[] message) {  
        Jedis jedis = null;  
        try {  
            jedis = jedisPool.getResource(); 
            jedis.publish(channel, message);  
        } catch (Exception e) {
        	logger.error("publish channel error:", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
  
    }  
}