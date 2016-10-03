package zeyin.cis550.hw5.schema;

import zeyin.cis550.hw5.data.IField;

public interface IAttribute<T> {
	/**
	 * Get the name of the attribute
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Set the attribute name
	 * 
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * The built-in datatypes
	 * 
	 * @author zives
	 *
	 */
	public enum TYPE {INT, DOUBLE, STRING};
	
	/**
	 * Attribute type info
	 * @return
	 */
	public TYPE getType();
	
	/**
	 * Create a blank field (attribute-value pair)
	 * for this attribute and set it to NULL
	 * 
	 * @return
	 */
	public IField<T> createField();
}
