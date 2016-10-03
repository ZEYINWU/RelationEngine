package zeyin.cis550.hw5.operators;

import zeyin.cis550.hw5.data.Tuple;

public class ConstInt implements IGetValue<Tuple,Integer> {
	Integer value;
	
	public ConstInt(Integer value) {
		this.value = value;
	}

	@Override
	public void setSource(Tuple source) {
		
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	public String toString() {
		return value.toString();
	}
}
