package com.yundaishidai.websoket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebsocketHandlerImp extends ChannelInitializer<SocketChannel> {


	protected void initChannel(SocketChannel ch) throws Exception {
		// http解码,websocket是以http发起请求的
		ch.pipeline().addLast(new HttpServerCodec());
		// HttpObjectAggregator会把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
		// 将HTTP消息的多个部分合成一条完整的HTTP消息
		ch.pipeline().addLast(new HttpObjectAggregator(65536));
		// 向客户端发送HTML5文件
		ch.pipeline().addLast(new ChunkedWriteHandler());
		// WebsocketHandler 自己定义的类，用以处理传过来的数据
		ch.pipeline().addLast(new WebsocketHandler());
	}
}