

import rpckids.server.RPCServer;


/**
 * 服务器端入口
 * @author gongzhihao
 *
 */
public class DemoServer {

	public static void main(String[] args) {
		RPCServer server = new RPCServer("localhost", 8888, 2, 16);  //监听客户端的端口
		server.service("fib", Integer.class, new FibRequestHandler()).service("exp", ExpRequest.class,
				new ExpRequestHandler());//注册服务
		server.start();
	}

}
