package rpckids.demo;

/**
 * 计算指数
 * 输入数据
 * @author gongzhihao
 *
 */
public class ExpRequest {
	private int base; //指数基数
	private int exp; //指数

	public ExpRequest() {
	}

	public ExpRequest(int base, int exp) {
		this.base = base;
		this.exp = exp;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}
}
