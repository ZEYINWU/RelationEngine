package zeyin.cis550.hw5.schema;

import zeyin.cis550.hw5.data.DoubleField;
import zeyin.cis550.hw5.data.IField;

public class DoubleAttribute extends BasicAttribute<Double> {
	public DoubleAttribute(String name) {
		setName(name);
	}

	@Override
	public zeyin.cis550.hw5.schema.IAttribute.TYPE getType() {
		return TYPE.DOUBLE;
	}

	@Override
	public IField<Double> createField() {
		return new DoubleField(this);
	}

}
