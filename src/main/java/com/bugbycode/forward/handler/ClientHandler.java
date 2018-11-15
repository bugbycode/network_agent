package com.bugbycode.forward.handler;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bugbycode.agent.handler.AgentHandler;
import com.bugbycode.module.Message;
import com.bugbycode.module.MessageCode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	
	private final Logger logger = LogManager.getLogger(ClientHandler.class);
	
	private Map<String,AgentHandler> agentHandlerMap;

	public ClientHandler(Map<String, AgentHandler> agentHandlerMap) {
		this.agentHandlerMap = agentHandlerMap;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		Set<String> set = agentHandlerMap.keySet();
		for(String token : set) {
			AgentHandler handler = agentHandlerMap.get(token);
			Message message = new Message(token, MessageCode.CLOSE_CONNECTION, null);
			handler.sendMessage(message);
		}
		agentHandlerMap.clear();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message message = (Message) msg;
		int type = message.getType();
		String token = message.getToken();
		if(type == MessageCode.CLOSE_CONNECTION) {
			
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				logger.debug("Heartbeat timeout.");
			} else if (event.state() == IdleState.WRITER_IDLE) {
				Message msg = new Message();
				msg.setType(MessageCode.HEARTBEAT);
				ctx.channel().writeAndFlush(msg);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
        logger.error(cause.getMessage());
	}
	
	
}
