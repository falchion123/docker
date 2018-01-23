package com.yundaishidai.redis.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yundaishidai.push.impl.PushManagerImpl;
import com.yundaishidai.redis.listener.MsgListener;
import com.yundaishidai.redis.sub.Subscriber;

import redis.clients.jedis.JedisPool;

public class Task extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(Task.class);
	

	
    private  Subscriber subscriber ;

    private  String channel;

   

    public Task(JedisPool jedisPool, String channel) {
    	 super();
         this.channel = channel;
         this.subscriber = new Subscriber(jedisPool);
	}

	@Override
    public void run() {
		MsgListener listener = new MsgListener(new PushManagerImpl());
		subscriber.subscribe(listener, channel);
    }
}