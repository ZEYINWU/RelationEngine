package zeyin.cis550.hw5.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zeyin.cis550.hw5.operators.IGetValue;
import zeyin.cis550.hw5.schema.IAttribute;
import zeyin.cis550.hw5.schema.Schema;
import zeyin.cis550.hw5.schema.TypeException;

public class Tuple implements Comparable<Tuple> {
	Schema schema;
	List<IField<?>> fields;
	Map<String, IField<?>> names;
	Map<IAttribute<?>, IField<?>> attribs;
	
	public Tuple(Schema schema) {
		this.schema = schema;
		names = new HashMap<String, IField<?>>();
		fields = new ArrayList<IField<?>>();
		attribs = new HashMap<IAttribute<?>, IField<?>>();
		
		schema.createFieldsForTuple(this);
	}
	
	public Tuple(Schema schema, List<IField<?>> fields) {
		this.schema = schema;
		this.fields = fields;
		names = new HashMap<String, IField<?>>();
		attribs = new HashMap<IAttribute<?>, IField<?>>();
		for (IField<?> f: fields) {
			names.put(f.getAttribute().getName(), f);
			attribs.put(f.getAttribute(), f);
		}
	}
	
	public List<IField<?>> getFields() {
		return fields;
	}
	
	public IField<?> getField(String name) {
		return names.get(name);
	}
	
	public IField<?> getField(int index) {
		return fields.get(index);
	}
	
	public int getNumFields() {
		return fields.size();
	}
	
	public void setFields(List<IField<?>> fields) {
		attribs.clear();
		names.clear();
		fields.clear();
		for (IField<?> f: fields)
			addField(f);
	}
	
	public void addField(IField<?> field) {
		
		// If we already have a field with this name, replace it
		if (names.containsKey(field.getAttribute().getName())) {
			IField<?> existing = names.get(field.getAttribute().getName());

			for (int i = 0; i < fields.size(); i++)
				if (fields.get(i) == existing)
					fields.set(i, field);
		} else {
			fields.add(field);
		}
		names.put(field.getAttribute().getName(), field);
		attribs.put(field.getAttribute(), field);
	}
	
	public List<IField<?>> getKey() {
		// No defined key
		if (schema.getNumKeys() == 0)
			return getFields();
		
		List<IField<?>> ret = new ArrayList<IField<?>>();
		
		for (IAttribute<?> key: schema.getKeys())
			ret.add(attribs.get(key));

		return ret;
	}
	
	public int hashCode() {
		int ret = 0;
		for (IAttribute<?> key: schema.getKeys())
			ret ^= attribs.get(key).getValue().hashCode();
		
		return ret;
	}
	
	/**
	 * Determine equivalence / ordering of tuples based on
	 * keys ONLY
	 */
	public int compareTo(Tuple t2) {
		List<IField<?>> ourKeys = getKey();
		List<IField<?>> theirKeys = t2.getKey();
		
		for (int i = 0; i < ourKeys.size(); i++) {
			int cmp = ourKeys.get(i).compare(theirKeys.get(i));
			if (cmp != 0)
				return cmp;
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(List<String> values) throws TypeException {
		for (int i = 0; i < values.size() && i < fields.size(); i++) {
			String v = values.get(i).toString();
			
			if (v.equals("null"))
				fields.get(i).setNull();
			else if (fields.get(i).getAttribute().getType() == IAttribute.TYPE.STRING) {
				if (v.startsWith("\'"))
					v = v.substring(1, v.length() - 1);
				((IField<String>)fields.get(i)).setValue(v);
			} else if (fields.get(i).getAttribute().getType() == IAttribute.TYPE.INT) {
				((IField<Integer>)fields.get(i)).setValue(Integer.valueOf(v));
			} else if (fields.get(i).getAttribute().getType() == IAttribute.TYPE.DOUBLE) {
				((IField<Double>)fields.get(i)).setValue(Double.valueOf(v));
			} else
				throw new TypeException("Unknown type for field " + fields.get(i).getAttribute());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(schema.getName() + ": (");
		
		for (int i = 0; i < fields.size(); i++) {
			if (i > 0)
				ret.append(", ");
			
			ret.append(fields.get(i).toString());
		}
		ret.append(")");
		
		return ret.toString();
	}
	
	public static IGetValue<Tuple,String> stringGetter(final String field) {
		return new IGetValue<Tuple,String>() {
			
			Tuple source;
			String fieldName = field;
			
			@Override
			public void setSource(Tuple source) {
				this.source = source;
			}

			@Override
			public String getValue() {
				return (String)source.getField(fieldName).getValue();
			}
			
			public boolean isNull() {
				return source.getField(fieldName).isNull();
			}

			@Override
			public String toString() {
				return "$" + fieldName;
			}
		};
	}

	public static IGetValue<Tuple,Integer> intGetter(final String field) {
		return new IGetValue<Tuple,Integer>() {
			
			Tuple source;
			String fieldName = field;
			
			@Override
			public void setSource(Tuple source) {
				this.source = source;
			}

			@Override
			public Integer getValue() {
				Object value = source.getField(fieldName).getValue();
				if (value instanceof Integer)
					return (Integer)value;
				else
					return Integer.valueOf(value.toString());
			}
			
			public boolean isNull() {
				return source.getField(fieldName).isNull();
			}
		
			@Override
			public String toString() {
				return "$" + fieldName;
			}
		};
	}

	public static IGetValue<Tuple,Double> doubleGetter(final String field) {
		return new IGetValue<Tuple,Double>() {
			
			Tuple source;
			String fieldName = field;
			
			@Override
			public void setSource(Tuple source) {
				this.source = source;
			}

			@Override
			public Double getValue() {
				return Double.valueOf(source.getField(fieldName).getValue().toString());
			}
			
			public boolean isNull() {
				return source.getField(fieldName).isNull();
			}

			@Override
			public String toString() {
				return "$" + fieldName;
			}
		};
	}
}
