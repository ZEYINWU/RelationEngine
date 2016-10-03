package zeyin.cis550.hw5.operators;

import zeyin.cis550.hw5.data.Tuple;

public class LiteralString implements IGetValue<Tuple,String> {
	String value;
	
	public LiteralString(String value) {
		this.value = value;
	}

	@Override
	public void setSource(Tuple source) {
		
	}

	@Override
	public String getValue() {
		return value;
	}

	public boolean isNull() {
		return false;
	}

	@Override
	public String toString() {
		return value;
	}
}
