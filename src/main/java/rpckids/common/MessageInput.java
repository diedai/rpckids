package rpckids.common;

import com.alibaba.fastjson.JSON;

/**
 * 消息输入
 * 
 * ---
 * 各个字段的作用？
 * 
 * ---
 * 与请求和响应的区别？
 * @author gongzhihao
 *
 */
public class MessageInput {
	private String type; //消息类型
	private String requestId; //请求id
	private String payload; //有效负载

	public MessageInput(String type, String requestId, String payload) {
		this.type = type;
		this.requestId = requestId;
		this.payload = payload;
	}

	public String getType() {
		return type;
	}

	public String getRequestId() {
		return requestId;
	}

	public <T> T getPayload(Class<T> clazz) {
		if (payload == null) {
			return null;
		}
		return JSON.parseObject(payload, clazz);
	}

}
