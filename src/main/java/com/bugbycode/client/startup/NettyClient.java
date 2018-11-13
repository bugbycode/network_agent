package com.bugbycode.client.startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bugbycode.client.handler.ClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	
	private final Logger logger = LogManager.getLogger(NettyClient.class);
	
	private ChannelFuture future;
	
	private Bootstrap remoteClient;
	
	private EventLoopGroup remoteGroup;
	
	private String host = "";
	
	private int port;
	
	public NettyClient(String host,int port) {
		this.host = host;
		this.port = port;
		this.remoteClient = new Bootstrap();
		this.remoteGroup = new NioEventLoopGroup();
	}
	
	public void connection() {
		this.remoteClient.group(remoteGroup).channel(NioSocketChannel.class);
		this.remoteClient.option(ChannelOption.TCP_NODELAY, true);
		this.remoteClient.option(ChannelOption.SO_KEEPALIVE, true);
		this.remoteClient.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new ClientHandler());
			}
		});
		
		future = this.remoteClient.connect(host, port).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					logger.info("Connection to " + host + ":" + port + " successfully.");
				}else {
					logger.info("Connection to " + host + ":" + port + " failed.");
				}
			}
		});
	}
	
	public void writeAndFlush(Object msg) {
		if(future == null) {
			return;
		}
		future.channel().writeAndFlush(msg);
	}
	
	public void close() {
		if(future == null) {
			return;
		}
		future.channel().close();
		
		if(remoteGroup == null) {
			return;
		}
		
		remoteGroup.shutdownGracefully();
		logger.info("Disconnection to " + host + ":" + port + " .");
	}
}
