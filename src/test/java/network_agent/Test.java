package network_agent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import io.netty.util.NettyRuntime;

public class Test {

	public static void main(String[] args) throws Exception {
		//System.out.println(NettyRuntime.availableProcessors());
//		URL url = new URL("http://www.baidu.com");
//		System.out.println(url.getPath());;
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 50000));
//		URLConnection con = url.openConnection(proxy);
//		InputStream in = con.getInputStream();
//		InputStreamReader isr = new InputStreamReader(in, "UTF-8");
//		BufferedReader br = new BufferedReader(isr);
//		String line = null;
//		while((line = br.readLine()) != null) {
//			System.out.println(line);
//		}
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",50000));
		Socket socket = new Socket(proxy);
		socket.connect(new InetSocketAddress("192.168.1.37",21));
		InputStream in = socket.getInputStream();
		byte[] buff = new byte[1024];
		int len = -1;
		while((len = in.read(buff)) != -1) {
			System.out.println(new String(buff,0,len));
		}
	}

}
