
/**
 * 计算指数
 * 输出数据
 * @author gongzhihao
 *
 */
public class ExpResponse {

	private long value; //值
	private long costInNanos; //花费时间（纳秒）

	public ExpResponse() {
	}

	public ExpResponse(long value, long costInNanos) {
		this.value = value;
		this.costInNanos = costInNanos;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getCostInNanos() {
		return costInNanos;
	}

	public void setCostInNanos(long costInNanos) {
		this.costInNanos = costInNanos;
	}

}
