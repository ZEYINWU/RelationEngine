package zeyin.cis550.hw5.data;

import zeyin.cis550.hw5.schema.IAttribute;

/**
 * A field (attribute-value) of type string
 * 
 * @author zives
 *
 */
public class StringField extends Field<String> {
	/**
	 * Constructor for field with value
	 * 
	 * @param type
	 * @param value
	 */
	public StringField(IAttribute<String> type, String value) {
		setAttribute(type);
		setValue(value);
	}

	/**
	 * Constructor for null field
	 * 
	 * @param type
	 */
	public StringField(IAttribute<String> type) {
		setAttribute(type);
		setNull();
		value = "";
	}

	@Override
	public int compareTo(IField<String> o) {
		if (!isNull() && !o.isNull())
			return getValue().compareTo(o.getValue());
		else
			return -1;
	}

	public int compare(IField<?> field) {
		if (field.getValue() instanceof String)
			return compareTo((StringField)field);
		else
			return -1;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof StringField && (compareTo((StringField)o) == 0))
			return true;
		else
			return false;
	}
	
	@Override
	public String toString() {
		if (isNull())
			return super.toString();
		else
			return getAttribute().getName() + ": " + "'" + 
				getValue().toString().replace("\'", "\'\'") + "'";
	}
}
