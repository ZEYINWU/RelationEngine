package zeyin.cis550.hw5.operators;

import zeyin.cis550.hw5.data.Tuple;

public class ConstDouble implements IGetValue<Tuple,Double> {
	Double value;
	
	public ConstDouble(Double value) {
		this.value = value;
	}

	@Override
	public void setSource(Tuple source) {
		
	}

	@Override
	public Double getValue() {
		return value;
	}
	
	public boolean isNull() {
		return false;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
