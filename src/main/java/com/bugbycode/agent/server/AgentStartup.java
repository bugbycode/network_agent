package com.bugbycode.agent.server;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.bugbycode.client.startup.NettyClient;

@Component
@Configuration
public class AgentStartup implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		AgentServer server = new AgentServer(50000, null, null);
		new Thread(server).start();
		new NettyClient("192.168.1.38", 443).connection();
	}

}
