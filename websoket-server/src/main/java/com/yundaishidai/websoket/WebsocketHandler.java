package com.yundaishidai.websoket;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class WebsocketHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = LoggerFactory.getLogger(WebsocketHandler.class);
	
	private WebSocketServerHandshaker handshaker;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);;
		System.out.println("�ͻ������������ӿ���");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	System.out.println("�ͻ������������ӹر�");
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("start.....");
		// ��ͳ��HTTP����
		if (msg instanceof FullHttpRequest) {
			logger.info("http");
			handleHttpRequest(ctx, (FullHttpRequest) msg);
			ChannelHandlerPool.channelGroup.add(ctx.channel());
		}else if (msg instanceof WebSocketFrame) {
			logger.info("websoket");
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		logger.info("end.....");
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		 printReqHeaders(req);
		// ���HTTP����ʧ�ܣ�����HHTP�쳣
		if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}

		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://"+req.headers().get(HttpHeaders.Names.HOST),
				null,false );
//		WebSocketServerHandshaker wsShakerHandler = wsShakerFactory.newHandshaker(req);
		
//		// ����������Ӧ���أ���������
//		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost/websocket",
//				null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
		
	}


	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// �ж��Ƿ��ǹر���·��ָ��
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			ChannelHandlerPool.channelGroup.remove(ctx.channel());
			return;
		}
		// �ж��Ƿ���Ping��Ϣ
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// �����̽�֧���ı���Ϣ����֧�ֶ�������Ϣ
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}

		// ����Ӧ����Ϣ
		String request = ((TextWebSocketFrame) frame).text();

		logger.info(String.format("%s received %s", ctx.channel(), request));

		ctx.channel().write(
				new TextWebSocketFrame(request + " , ��ӭʹ��Netty WebSocket��������ʱ�̣�" + new java.util.Date().toString()));
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp) {
		// ����Ӧ����ͻ���
		if (resp.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(resp.getStatus().toString(), CharsetUtil.UTF_8);
			resp.content().writeBytes(buf);
			buf.release();
			HttpUtil.setContentLength(resp, resp.content().readableBytes());
		}

		// ����Ƿ�Keep-Alive���ر�����
		ChannelFuture f = ctx.channel().writeAndFlush(resp);
		if (!HttpUtil.isKeepAlive(req) || resp.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private void printReqHeaders(FullHttpRequest req) {
		HttpHeaders headers = req.headers();
	
			List<Map.Entry<String,String>> ls = headers.entries();
			for(Map.Entry<String,String> i: ls){
				logger.info("header  "+i.getKey()+":"+i.getValue());
			}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}