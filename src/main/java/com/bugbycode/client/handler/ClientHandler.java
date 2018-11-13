package com.bugbycode.client.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private final Logger logger = LogManager.getLogger(ClientHandler.class);
	
	public ClientHandler() {
		
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		
	}

	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
		super.channelInactive(ctx);
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
		logger.error(cause.getMessage());
    }
}
