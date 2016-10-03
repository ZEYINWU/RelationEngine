package zeyin.cis550.hw5.schema;

import zeyin.cis550.hw5.data.IField;
import zeyin.cis550.hw5.data.IntField;

public class IntAttribute extends BasicAttribute<Integer> {
	public IntAttribute(String name) {
		setName(name);
	}
	
	@Override
	public TYPE getType() {
		return TYPE.INT;
	}

	@Override
	public IField<Integer> createField() {
		return new IntField(this);
	}
}
