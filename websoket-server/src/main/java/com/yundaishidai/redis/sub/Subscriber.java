package com.yundaishidai.redis.sub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class Subscriber {
	
	private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
	
	private final JedisPool jedisPool;

	public Subscriber(JedisPool jedisPool) {
		super();
		this.jedisPool = jedisPool;
	}

	
	/**
	 * 
	 * @param jedisPubSub
	 * @param channel
	 */
    public void subscribe(JedisPubSub jedisPubSub,String channel) {  
        Jedis jedis = null;  
        try {  
            jedis = jedisPool.getResource(); 
            jedis.subscribe(jedisPubSub,channel);  
        } catch (Exception e) {
        	logger.error("subsrcibe channel error:", e); 
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    } 
}
