package zeyin.cis550.hw5.data;

import zeyin.cis550.hw5.schema.IAttribute;

/**
 * A field (attribute-value pair) for a double
 * 
 * @author zives
 *
 */
public class DoubleField extends Field<Double> {
	/**
	 * Constructor for field with value
	 * 
	 * @param type
	 * @param value
	 */
	public DoubleField(IAttribute<Double> type, Double value) {
		setAttribute(type);
		setValue(value);
	}
	
	/**
	 * Constructor for null field
	 * @param type
	 */
	public DoubleField(IAttribute<Double> type) {
		setAttribute(type);
		setNull();
		value = new Double(0);
	}

	@Override
	public int compareTo(IField<Double> o) {
		if (!isNull() && !o.isNull())
			return getValue().compareTo(o.getValue());
		else
			return -1;
	}
	
	public int compare(IField<?> field) {
		if (field.getValue() instanceof Double)
			return compareTo((DoubleField)field);
		else
			return -1;
	}
	

	@Override
	public boolean equals(Object o) {
		if (o instanceof DoubleField && (compareTo((DoubleField)o) == 0))
			return true;
		else
			return false;
	}
}
