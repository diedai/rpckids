package rpckids.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpckids.common.IMessageHandler;
import rpckids.common.MessageHandlers;
import rpckids.common.MessageInput;
import rpckids.common.MessageRegistry;

/**
 * 读写数据-服务器端
 * @author gongzhihao
 *
 */
@Sharable
public class MessageCollector extends ChannelInboundHandlerAdapter {

	private final static Logger LOG = LoggerFactory.getLogger(MessageCollector.class);

	private ThreadPoolExecutor executor;
	
	private MessageHandlers handlers;
	private MessageRegistry registry;

	public MessageCollector(MessageHandlers handlers, MessageRegistry registry, int workerThreads) {
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000); //阻塞队列
		
		ThreadFactory factory = new ThreadFactory() { //线程工厂类

			AtomicInteger seq = new AtomicInteger();

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("rpc-" + seq.getAndIncrement());
				return t;
			}

		};
		
		this.executor = new ThreadPoolExecutor(1, workerThreads, 30, TimeUnit.SECONDS, queue, factory,
				new CallerRunsPolicy()); //线程池类
		
		this.handlers = handlers;
		this.registry = registry;
	}

	public void closeGracefully() {
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		this.executor.shutdownNow();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOG.debug("connection comes");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOG.debug("connection leaves");
	}

	/**
	 * 从客户端读数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { //第二个参数的数据来源？服务器和客户端初始化时 已经添加编码器和解码器；读写该数据的时候 直接调用读写方法即可；服务器和客户端都是如此
		if (msg instanceof MessageInput) {
			this.executor.execute(() -> {
				this.handleMessage(ctx, (MessageInput) msg); //处理数据
			});
		}
	}

	/**
	 * 处理数据
	 * @param ctx
	 * @param input
	 */
	private void handleMessage(ChannelHandlerContext ctx, MessageInput input) {
		// 业务逻辑在这里
		Class<?> clazz = registry.get(input.getType()); //service类型——》service类
		if (clazz == null) {
			handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
			return;
		}
		Object o = input.getPayload(clazz); //输入数据
		@SuppressWarnings("unchecked")
		IMessageHandler<Object> handler = (IMessageHandler<Object>) handlers.get(input.getType()); //服务提供者
		if (handler != null) {
			handler.handle(ctx, input.getRequestId(), o); //处理数据
		} else { //默认服务提供者
			handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOG.warn("connection error", cause);
	}

}
