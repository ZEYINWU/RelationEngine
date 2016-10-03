package zeyin.cis550.hw5.schema;

import zeyin.cis550.hw5.data.IField;
import zeyin.cis550.hw5.data.StringField;

public class StringAttribute extends BasicAttribute<String> {
	public StringAttribute(String name) {
		setName(name);
	}

	@Override
	public zeyin.cis550.hw5.schema.IAttribute.TYPE getType() {
		return TYPE.STRING;
	}

	@Override
	public IField<String> createField() {
		return new StringField(this);
	}

}
