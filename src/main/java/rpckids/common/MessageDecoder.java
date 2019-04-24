package rpckids.common;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * 消息解码
 * @author gongzhihao
 *
 */
public class MessageDecoder extends ReplayingDecoder<MessageInput> {

	/**
	 * 读数据就是解码
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		String requestId = readStr(in);
		String type = readStr(in);
		String content = readStr(in);
		out.add(new MessageInput(type, requestId, content)); //二进制数据——》对象数据
	}

	private String readStr(ByteBuf in) {
		int len = in.readInt();
		if (len < 0 || len > (1 << 20)) {
			throw new DecoderException("string too long len=" + len);
		}
		byte[] bytes = new byte[len];
		in.readBytes(bytes);
		return new String(bytes, Charsets.UTF8);
	}

}
