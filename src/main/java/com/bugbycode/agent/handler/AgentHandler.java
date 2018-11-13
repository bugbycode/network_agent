package com.bugbycode.agent.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bugbycode.client.handler.ClientHandler;
import com.bugbycode.client.startup.NettyClient;
import com.bugbycode.module.Protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AgentHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private final Logger logger = LogManager.getLogger(AgentHandler.class);
	
	private Map<String,AgentHandler> agentHandlerMap;
	
	private Map<String,ClientHandler> clientHandlerMap;
	
	private Socket socket = null;
	
	private InputStream in = null;
	
	private OutputStream out = null;
	
	private boolean firstConnect = false;
	
	public AgentHandler(Map<String, AgentHandler> agentHandlerMap, Map<String, ClientHandler> clientHandlerMap) {
		this.agentHandlerMap = agentHandlerMap;
		this.clientHandlerMap = clientHandlerMap;
		this.firstConnect = true;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		byte[] data = new byte[msg.readableBytes()];
		msg.readBytes(data);
		Properties pro = System.getProperties();
		System.out.println(pro);
		if(firstConnect) {
			firstConnect = false;
			String connectionStr = new String(data).trim();
			System.out.println("data : " + connectionStr);
			String[] connectArr = connectionStr.split("\r\n");
			String protocolData = connectArr[0];
			String serverData = connectArr[1];
			int port = 443;
			String host = "";
			int protocol = Protocol.HTTP;
			if(protocolData.startsWith("GET")) {
				if(protocolData.startsWith("GET ftp:")) {
					protocol = Protocol.FTP;
				}
			}else if(protocolData.startsWith("CONNECT")) {
				protocol = Protocol.HTTPS;
			}else {
				throw new RuntimeException("协议错误");
			}
			
			if(serverData.startsWith("Host:")) {
				String[] serverArr = serverData.split(":");
				int len = serverArr.length;
				if(len == 2) {
					if(protocol == Protocol.HTTPS) {
						port = 443;
					}else if(protocol == Protocol.HTTP) {
						port = 80;
					}else if(protocol == Protocol.FTP) {
						port = 21;
					}
					host = serverArr[1];
				}else if(len == 3) {
					port = Integer.valueOf(serverArr[2]);
					host = serverArr[1];
				}else {
					throw new RuntimeException("主机信息错误");
				}
			}else {
				throw new RuntimeException("主机信息错误");
			}
			
			//new NettyClient(host,port).connection();
			
			if(protocol == Protocol.HTTPS) {
				String response = "HTTP/1.1 200 Connection Established\r\n\r\n";
				byte[] res = response.getBytes();
				ByteBuf buff = ctx.alloc().buffer(res.length);
				buff.writeBytes(res);
				ctx.channel().writeAndFlush(res);
			}else {
				//forward(data);
			}
			new MyThread(ctx.channel()).start();
		}else {
			//forward(data);
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//关闭连接
		if(socket != null) {
			socket.close();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage());
		cause.getStackTrace();
	}
	
	private class MyThread extends Thread{
		
		private Channel channel;
		
		public MyThread(Channel channel) {
			this.channel = channel;
		}

		@Override
		public void run() {
			byte[] buff = new byte[4096];
			int len = -1;
			try {
				while((len = in.read(buff)) != -1) {
					ByteBuf data = channel.alloc().buffer(len);
					data.writeBytes(buff, 0, len);
					channel.writeAndFlush(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void forward(byte[] data) {
		try {
			out.write(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
