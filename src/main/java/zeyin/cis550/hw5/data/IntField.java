package zeyin.cis550.hw5.data;

import zeyin.cis550.hw5.schema.IAttribute;

/**
 * A field (attrib-value pair) of type integer
 * @author zives
 *
 */
public class IntField extends Field<Integer> {
	/**
	 * Constructor for field with value
	 * 
	 * @param type
	 * @param value
	 */
	public IntField(IAttribute<Integer> type, Integer value) {
		setAttribute(type);
		setValue(value);
	}
	
	/**
	 * Constructor for null field
	 * 
	 * @param type
	 */
	public IntField(IAttribute<Integer> type) {
		setAttribute(type);
		setNull();
		value = new Integer(0);
	}

	@Override
	public int compareTo(IField<Integer> o) {
		if (!isNull() && !o.isNull())
			return getValue().compareTo(o.getValue());
		else
			return -1;
	}

	public int compare(IField<?> field) {
		if (field.getValue() instanceof Integer)
			return compareTo((IntField)field);
		else
			return -1;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof IntField && (compareTo((IntField)o) == 0))
			return true;
		else
			return false;
	}
}
