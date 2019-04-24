package rpckids.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 是什么？注册中心
 * 作用？注册服务提供者。//注：所谓注册服务，就是把服务暂时保存起来放到数据结构map里
 * @author gongzhihao
 *
 */
public class MessageHandlers {

	private Map<String, IMessageHandler<?>> handlers = new HashMap<>(); //实际处理器
	private IMessageHandler<MessageInput> defaultHandler; //默认处理器

	/**
	 * 注册服务提供者
	 * @param type 服务类型
	 * @param handler 服务提供者
	 */
	public void register(String type, IMessageHandler<?> handler) {
		handlers.put(type, handler);
	}

	/**
	 * 设置默认服务提供者
	 * @param defaultHandler 默认服务提供者
	 * @return
	 */
	public MessageHandlers defaultHandler(IMessageHandler<MessageInput> defaultHandler) {
		this.defaultHandler = defaultHandler;
		return this;
	}

	/**
	 * 获取默认服务提供者
	 * @return 默认服务提供者
	 */
	public IMessageHandler<MessageInput> defaultHandler() {
		return defaultHandler;
	}

	/**
	 * 获取服务提供者
	 * @param type 服务类型
	 * @return 服务提供者
	 */
	public IMessageHandler<?> get(String type) {
		IMessageHandler<?> handler = handlers.get(type);
		return handler;
	}

}
