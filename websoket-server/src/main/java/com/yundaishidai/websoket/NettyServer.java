package com.yundaishidai.websoket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yundaishidai.redis.task.Task;
import com.yundaishidai.utils.PropertiesUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class NettyServer {
	
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
	
    public void run(final int port) throws InterruptedException {
        
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            	.channel(NioServerSocketChannel.class)
                .childHandler(new WebsocketHandlerImp());
            
            JedisPool jedisPool = getRedisPool();
            Task task = new Task(jedisPool,"channel");
            task.start();
            logger.info("Netty server is ok!");
            
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
        	
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            logger.info("Netty server is down!");
        }
    }

	
    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        try {
            server.run(80);
        } catch (Exception e) {
          
            logger.error("Netty server is down!",e);
        }
    }
    
    
    private JedisPool getRedisPool() {
		String redisIp = PropertiesUtil.getKey("redis.host");
		int reidsPort = Integer.valueOf(PropertiesUtil.getKey("redis.port"));
		JedisPoolConfig config = new JedisPoolConfig();
		JedisPool jedisPool = new JedisPool(config, redisIp, reidsPort);
		logger.info(String.format("redis pool is starting, redis ip %s, redis port %d", redisIp, reidsPort));
		return jedisPool;
	}

}