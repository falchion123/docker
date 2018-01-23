package com.yundaishidai.websoket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebsocketHandlerImp extends ChannelInitializer<SocketChannel> {


	protected void initChannel(SocketChannel ch) throws Exception {
		// http����,websocket����http���������
		ch.pipeline().addLast(new HttpServerCodec());
		// HttpObjectAggregator��Ѷ����Ϣת��Ϊһ����һ��FullHttpRequest����FullHttpResponse
		// ��HTTP��Ϣ�Ķ�����ֺϳ�һ��������HTTP��Ϣ
		ch.pipeline().addLast(new HttpObjectAggregator(65536));
		// ��ͻ��˷���HTML5�ļ�
		ch.pipeline().addLast(new ChunkedWriteHandler());
		// WebsocketHandler �Լ�������࣬���Դ�������������
		ch.pipeline().addLast(new WebsocketHandler());
	}
}