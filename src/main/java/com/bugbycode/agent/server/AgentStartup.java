package com.bugbycode.agent.server;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.bugbycode.agent.handler.AgentHandler;
import com.bugbycode.client.startup.NettyClient;
import com.bugbycode.thread.LocalClientThreadPool;

@Component
@Configuration
public class AgentStartup implements ApplicationRunner {

	@Autowired
	private Map<String,AgentHandler> agentHandlerMap;
	
	@Autowired
	private Map<String,NettyClient> nettyClientMap;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		LocalClientThreadPool threadPool = new LocalClientThreadPool(agentHandlerMap, nettyClientMap);
		AgentServer server = new AgentServer(50000, agentHandlerMap,nettyClientMap,threadPool);
		new Thread(server).start();
	}

}
