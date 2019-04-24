package rpckids.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import rpckids.common.IMessageHandler;
import rpckids.common.MessageDecoder;
import rpckids.common.MessageEncoder;
import rpckids.common.MessageHandlers;
import rpckids.common.MessageRegistry;

/**
 * rpc框架的服务类
 * @author gongzhihao
 *
 */
public class RPCServer {

	private final static Logger LOG = LoggerFactory.getLogger(RPCServer.class);

	private String ip;
	private int port;
	
	private int ioThreads; //io线程的数量
	private int workerThreads; //work线程的数量
	
	private MessageHandlers handlers = new MessageHandlers();
	private MessageRegistry registry = new MessageRegistry(); //注册中心

	{
		handlers.defaultHandler(new DefaultHandler());
	}

	public RPCServer(String ip, int port, int ioThreads, int workerThreads) {
		this.ip = ip;
		this.port = port;
		this.ioThreads = ioThreads;
		this.workerThreads = workerThreads;
	}

	private ServerBootstrap bootstrap;
	private EventLoopGroup group;
	private MessageCollector collector;
	private Channel serverChannel;

	/**
	 * 注册服务
	 * @param type 服务类型
	 * @param reqClass 请求数据类型
	 * @param handler 服务提供者
	 * @return
	 */
	public RPCServer service(String type, Class<?> reqClass, IMessageHandler<?> handler) {
		//注册请求数据类型
		registry.register(type, reqClass);
		//注册服务提供者
		handlers.register(type, handler);
		return this;
	}

	public void start() {
		bootstrap = new ServerBootstrap();
		group = new NioEventLoopGroup(ioThreads);
		bootstrap.group(group);
		collector = new MessageCollector(handlers, registry, workerThreads);
		MessageEncoder encoder = new MessageEncoder();
		bootstrap.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipe = ch.pipeline();
				pipe.addLast(new ReadTimeoutHandler(60));
				pipe.addLast(new MessageDecoder()); //解码
				pipe.addLast(encoder); //编码
				pipe.addLast(collector);
			}
		});
		bootstrap.option(ChannelOption.SO_BACKLOG, 100).option(ChannelOption.SO_REUSEADDR, true)
				.option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_KEEPALIVE, true);
		serverChannel = bootstrap.bind(this.ip, this.port).channel(); //绑定ip/port
		LOG.warn("server started @ {}:{}\n", ip, port);
	}

	public void stop() {
		// 先关闭服务端套件字
		serverChannel.close();
		// 再斩断消息来源，停止io线程池
		group.shutdownGracefully();
		// 最后停止业务线程
		collector.closeGracefully();
	}

}