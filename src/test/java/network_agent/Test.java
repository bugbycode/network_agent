package network_agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.bugbycode.agent.server.AgentStartup;
import com.bugbycode.client.startup.NettyClient;

public class Test {

	public static void main(String[] args) throws Exception {
		new NettyClient("192.168.1.38", 443).connection();
//		Socket socket = new Socket("192.168.1.38",443);
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("GET http://localhost:8080/ HTTP/1.1\r\n" + 
//				"Host: localhost:8080\r\n" + 
//				"Proxy-Connection: keep-alive\r\n" + 
//				"Cache-Control: max-age=0\r\n" + 
//				"Upgrade-Insecure-Requests: 1\r\n" + 
//				"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36\r\n" + 
//				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" + 
//				"Accept-Encoding: gzip, deflate, br\r\n" + 
//				"Accept-Language: zh-CN,zh;q=0.9\r\n" + 
//				"Cookie: _ga=GA1.1.1143512757.1533787418; _gid=GA1.1.1340224937.1541992630\r\n");
//		OutputStream out = socket.getOutputStream();
//		out.write(buffer.toString().getBytes());
//		out.flush();
//		InputStream in = socket.getInputStream();
//		byte[] buff = new byte[4096];
//		int len = -1;
//		while((len = in.read(buff)) != -1) {
//			System.out.println(new String(buff,0,len));
//		}
	}

}
