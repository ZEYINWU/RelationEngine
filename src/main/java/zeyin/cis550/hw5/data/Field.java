package zeyin.cis550.hw5.data;

import zeyin.cis550.hw5.schema.IAttribute;

/**
 * Base class for all fields
 * 
 * @author zives
 *
 * @param <T>
 */
public abstract class Field<T> implements IField<T> {
	/**
	 * Attribute type info
	 */
	IAttribute<T> attribute;
	
	/**
	 * Attribute value
	 */
	T value;
	
	/**
	 * Null status
	 */
	boolean isNull;
	
	@Override
	public void setAttribute(IAttribute<T> attrib) {
		this.attribute = attrib;
	}

	@Override
	public IAttribute<T> getAttribute() {
		return attribute;
	}

	/**
	 * Getter for the actual value (undefined if isNull())
	 */
	@Override
	public T getValue() {
		return value;
	}
	
	/**
	 * Setter for the actual value, automatically makes it non-null
	 */
	@Override
	public void setValue(T value) {
		this.value = value;
		clearNull();
	}

	@Override
	public boolean isNull() {
		return isNull;
	}

	@Override
	public void setNull() {
		isNull = true;
	}

	@Override
	public void clearNull() {
		isNull = false;
	}

	@Override
	public void setNull(boolean state) {
		isNull = state;
	}
	
	/**
	 * Hashcode is the attribute hashcode XORed with the value hashcode
	 */
	@Override
	public int hashCode() {
		if (isNull())
			return getAttribute().hashCode();
		else
			return getAttribute().hashCode() ^
					getValue().hashCode();
	}

	@Override
	public String toString() {
		if (isNull())
			return getAttribute().getName() + ": " + "NULL";
		else
			return getAttribute().getName() + ": " + getValue().toString();
	}
	
}
