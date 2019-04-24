package rpckids.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理器
 * @author gongzhihao
 *
 * @param <T>
 */
@FunctionalInterface
public interface IMessageHandler<T> {

	/**
	 * 服务提供者的方法
	 * @param ctx
	 * @param requestId 请求id
	 * @param message 请求数据
	 */
	void handle(ChannelHandlerContext ctx, String requestId, T message);

}
