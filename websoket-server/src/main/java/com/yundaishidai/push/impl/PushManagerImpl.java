package com.yundaishidai.push.impl;

import com.yundaishidai.push.PushManager;
import com.yundaishidai.websoket.ChannelHandlerPool;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class PushManagerImpl implements PushManager {
	
	public void push(Object msg) {
		System.out.println("start push msg:"+msg);
		ChannelHandlerPool.channelGroup.writeAndFlush(new TextWebSocketFrame((String) msg));
		System.out.println("end push msg");
	}

}
