package com.bugbycode.thread;

import java.util.LinkedList;
import java.util.Map;

import com.bugbycode.agent.handler.AgentHandler;
import com.bugbycode.client.startup.NettyClient;
import com.bugbycode.module.Message;
import com.bugbycode.module.MessageCode;
import com.util.RandomUtil;

public class LocalClientThreadPool extends ThreadGroup {

	private Map<String,AgentHandler> agentHandlerMap;
	
	private Map<String,NettyClient> nettyClientMap;
	
	private LinkedList<Message> queue;
	
	private final int THREAD_SIZE = 10; 
	
	private boolean isClosed = true;

	public LocalClientThreadPool(Map<String, AgentHandler> agentHandlerMap,
			Map<String,NettyClient> nettyClientMap) {
		super(RandomUtil.GetGuid32());
		this.agentHandlerMap = agentHandlerMap;
		this.nettyClientMap = nettyClientMap;
		this.queue = new LinkedList<Message>();
		start();
	}
	
	public synchronized void addMessage(Message msg) {
		if(isClosed) {
			return;
		}
		if(msg.getType() != MessageCode.CONNECTION) {
			return;
		}
		this.queue.addLast(msg);
		this.notifyAll();
	}
	
	private synchronized Message getMessage() throws InterruptedException {
		while(queue.isEmpty()) {
			if(isClosed) {
				throw new InterruptedException("Thread pool closed.");
			}
			wait();
		}
		return queue.removeFirst();
	}
	
	public void start() {
		for(int i = 0;i < THREAD_SIZE;i++) {
			new WorkThread().start();
		}
		this.isClosed = false;
	}
	
	private class WorkThread extends Thread{

		@Override
		public void run() {
			while(!isClosed) {
				try {
					Message message = getMessage();
					if(message == null) {
						continue;
					}
					new NettyClient(message, nettyClientMap, agentHandlerMap)
						.connection();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
