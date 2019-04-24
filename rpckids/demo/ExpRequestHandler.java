

import io.netty.channel.ChannelHandlerContext;
import rpckids.common.IMessageHandler;
import rpckids.common.MessageOutput;

/**
 * 服务提供者
 * 计算指数
 * @author gongzhihao
 *
 */
public class ExpRequestHandler implements IMessageHandler<ExpRequest> {

	@Override
	public void handle(ChannelHandlerContext ctx, String requestId, ExpRequest message) {
		int base = message.getBase();
		int exp = message.getExp();
		long start = System.nanoTime();
		long res = 1;
		for (int i = 0; i < exp; i++) {
			res *= base;
		}
		long cost = System.nanoTime() - start;
		ctx.writeAndFlush(new MessageOutput(requestId, "exp_res", new ExpResponse(res, cost)));
	}

}
