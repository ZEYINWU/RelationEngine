package zeyin.cis550.hw5.data;

import zeyin.cis550.hw5.schema.IAttribute;

public interface IField<T> extends Comparable<IField<T>>{
	/**
	 * What is the attribute (name & type)?
	 * @return
	 */
	public IAttribute<T> getAttribute();
	
	/**
	 * Set the attribute (name & type)
	 * 
	 * @param attrib
	 */
	public void setAttribute(IAttribute<T> attrib);
	
	/**
	 * Attribute value
	 * 
	 * @return
	 */
	public T getValue();
	
	public void setValue(T value);
	
	public boolean isNull();
	
	public void setNull();
	
	public void clearNull();
	
	public void setNull(boolean state);

	public int compare(IField<?> iField);
}
