package rpckids.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 是什么？注册中心
 * 作用？注册服务服务输入数据的数据类型。
 * @author gongzhihao
 *
 */
public class MessageRegistry {
	/**
	 * 
	 */
	private Map<String, Class<?>> clazzes = new HashMap<>();

	/**
	 * 注册服务输入数据的数据类型
	 * @param type 服务类型
	 * @param clazz 服务输入数据的数据类型
	 */
	public void register(String type, Class<?> clazz) {
		clazzes.put(type, clazz);
	}

	/**
	 * 获取服务输入数据的数据类型
	 * @param type 服务类型
	 * @return 服务输入数据的数据类型
	 */
	public Class<?> get(String type) {
		return clazzes.get(type);
	}
}
