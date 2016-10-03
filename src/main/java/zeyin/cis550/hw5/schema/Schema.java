package zeyin.cis550.hw5.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zeyin.cis550.hw5.data.Tuple;

/**
 * The schema for a relation
 * 
 * @author zives
 *
 */
public class Schema {
	String name;
	
	/**
	 * The attributes, in terms of name / type, in order
	 */
	List<IAttribute<?>> attributes;
	/**
	 * A mapping from attribute name to position
	 */
	Map<String, Integer> names;
	
	/**
	 * The list of attributes to be used as a key.  Order matters.
	 */
	List<IAttribute<?>> keys;
	
	/**
	 * Basic constructor -- we have a list of attributes
	 * @param attributes
	 */
	public Schema(String name, List<IAttribute<?>> attributes) {
		this(name);
		for (IAttribute<?> attrib: attributes)
			addAttribute(attrib);
	}
	
	public Schema(String name) {
		this.name = name;
		
		this.attributes = new ArrayList<IAttribute<?>>();
		this.names = new HashMap<String, Integer>();
		this.keys = new ArrayList<IAttribute<?>>();
	}
	
	public int getNumAttributes() {
		return attributes.size();
	}
	
	public int getNumKeys() {
		if (keys.isEmpty())
			return attributes.size();
		else
			return keys.size();
	}
	
	/**
	 * Extend the schema with an attribute
	 * 
	 * @param attrib
	 */
	public void addAttribute(IAttribute<?> attrib) {
		int inx = attributes.size();
		attributes.add(attrib);
		names.put(attrib.getName(), inx);
	}
	
	/**
	 * Make the current attribute (which must be in the schema)
	 * a key
	 * 
	 * @param key
	 * @throws TypeException
	 */
	public void addKey(IAttribute<?> key) throws TypeException {
		keys.add(key);
		if (!attributes.contains(key))
			throw new TypeException("Key attribute " + 
					key.getName() + " must be in the schema!");
	}
	
	/**
	 * Returns the list of keys (all attributes if there's no
	 * specific key)
	 * 
	 * @return
	 */
	public List<IAttribute<?>> getKeys() {
		if (keys.isEmpty())
			return attributes;
		else
			return keys;
	}
	
	/**
	 * Get a key attribute by its index
	 * 
	 * @param i
	 * @return
	 */
	public IAttribute<?> getKey(int i) {
		return getKeys().get(i);
	}
	
	/**
	 * Get an attribute by its index
	 * 
	 * @param i
	 * @return
	 */
	public IAttribute<?> getAttribute(int i) {
		return attributes.get(i);
	}
	
	/**
	 * Get the type of the attribute at position i
	 * 
	 * @param i
	 * @return
	 */
	public IAttribute.TYPE getType(int i) {
		return attributes.get(i).getType();
	}
	
	/**
	 * Get the name of the attribute at position i
	 * 
	 * @param i
	 * @return
	 */
	public String getAttributeName(int i) {
		return attributes.get(i).getName();
	}
	
	/**
	 * Look up the position (index) of a named attribute,
	 * or throw an exception if not found
	 * 
	 * @param attributeName
	 * @return
	 * @throws TypeException if attribute not in schema
	 */
	public int getIndexOf(String attributeName) throws TypeException {
		if (names.containsKey(attributeName))
			return names.get(attributeName);
		else
			throw new TypeException("Attribute " + attributeName + " not found");
	}
	
	/**
	 * Returns an attribute by its name
	 * 
	 * @param attributeName
	 * @return
	 * @throws TypeException if attribute not found
	 */
	public IAttribute<?> getAttribute(String attributeName) 
	throws TypeException {
		return getAttribute(getIndexOf(attributeName));
	}
	
	/**
	 * Gets the type of an attribute by its name
	 * 
	 * @param attributeName
	 * @return
	 * @throws TypeException if attribute not found
	 */
	public IAttribute.TYPE getType(String attributeName) 
			throws TypeException {
		return getAttribute(attributeName).getType();
	}
	
	public void createFieldsForTuple(Tuple t) {
		for (IAttribute<?> att: attributes)
			t.addField(att.createField());
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getName() + "(");
		
		boolean first = true;
		for (IAttribute<?> attrib: attributes) {
			if (first)
				first = false;
			else
				builder.append(',');
			builder.append(attrib.toString());
		}
		
		builder.append(')');
		return builder.toString();
	}
}
