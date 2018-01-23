package com.yundaishidai.redis.listener;

import com.yundaishidai.push.PushManager;

import redis.clients.jedis.JedisPubSub;

public class MsgListener extends JedisPubSub {

	private PushManager pushMgr;

	public MsgListener() {
	}

	public MsgListener(PushManager pushMgr) {
		super();
		this.pushMgr = pushMgr;
	}

	public void onMessage(String channel, String message) {
		pushMgr.push(message);
		System.out.println(String.format("receive redis published message, channel %s, message %s", channel, message));
	}

	public void onSubscribe(String channel, int subscribedChannels) {
		System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d", channel,
				subscribedChannels));
	}

	public void onUnsubscribe(String channel, int subscribedChannels) {
		System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d", channel,
				subscribedChannels));

	}
}
