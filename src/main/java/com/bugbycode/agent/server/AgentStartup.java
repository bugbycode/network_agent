package com.bugbycode.agent.server;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.bugbycode.agent.handler.AgentHandler;
import com.bugbycode.client.startup.NettyClient;
import com.bugbycode.forward.client.StartupRunnable;

import io.netty.channel.EventLoopGroup;

@Component
@Configuration
public class AgentStartup implements ApplicationRunner {

	@Autowired
	private Map<String,AgentHandler> agentHandlerMap;
	
	@Autowired
	private Map<String,AgentHandler> forwardHandlerMap;
	
	@Autowired
	private Map<String,NettyClient> nettyClientMap;
	
	@Autowired
	private EventLoopGroup remoteGroup;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		StartupRunnable startup = new StartupRunnable("killbuff.com", 36500, "admin", "", forwardHandlerMap); 
		startup.run();
		AgentServer server = new AgentServer(50000, agentHandlerMap,forwardHandlerMap,nettyClientMap,remoteGroup,startup);
		new Thread(server).start();
	}

}
