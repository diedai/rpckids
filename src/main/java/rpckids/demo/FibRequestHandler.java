package rpckids.demo;


import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import rpckids.common.IMessageHandler;
import rpckids.common.MessageOutput;

/**
 * 服务提供者
 * 计算菲波齐纳数列
 * @author gongzhihao
 *
 */
public class FibRequestHandler implements IMessageHandler<Integer> {

	private List<Long> fibs = new ArrayList<>();

	{
		fibs.add(1L); // fib(0) = 1
		fibs.add(1L); // fib(1) = 1
	}

	@Override
	public void handle(ChannelHandlerContext ctx, String requestId, Integer n) {
		//处理数据
		for (int i = fibs.size(); i < n + 1; i++) {
			long value = fibs.get(i - 2) + fibs.get(i - 1);
			fibs.add(value);
		}
		
		ctx.writeAndFlush(new MessageOutput(requestId, "fib_res", fibs.get(n))); //写数据到客户端
	}

}