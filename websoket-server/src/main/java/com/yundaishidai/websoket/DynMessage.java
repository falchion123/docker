package com.yundaishidai.websoket;

import java.util.Random;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
*动态产生消息，并向Channel组推送。
*/
public class DynMessage implements Runnable{
	public static ChannelGroup audiences = new DefaultChannelGroup("msg-group", null);
	
	static public void addAudience(Channel ch){		
		audiences.add(ch);
	}
	
	static public void removeAudience(Channel ch){
		audiences.remove(ch);
	}
	
	static String[] names = {
		"Tom", "Jerry",
		"Terry", "Looney",
		"Merrie", "William",
		"Joseph", "Hanna",
		"Speike", "Tyke",
		"Tuffy", "Lightning",
	};
	static String message = "";
	
	public static String getMessage(){
		StringBuffer sb = new StringBuffer();
		sb.append("hello,my name is ");
		sb.append(names[new Random().nextInt(names.length)]);
		sb.append(".");		
		return sb.toString();
	}

	public void run() {		
		System.out.println("DynMessage start");
		for(;;){
			String msg = getMessage();			
			radiate(msg);
			try{Thread.sleep(1000); }catch(Exception ex){}
		}
	}
	
	void radiate(String msg){
		audiences.write(new TextWebSocketFrame(msg));
	}
}