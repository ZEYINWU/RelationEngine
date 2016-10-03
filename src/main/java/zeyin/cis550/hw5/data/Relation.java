package zeyin.cis550.hw5.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import zeyin.cis550.hw5.schema.Schema;

public class Relation {
	Schema schema;
	
	Collection<Tuple> tuples;
	
	public Relation(Schema schema) {
		this.schema = schema;
		
		tuples = new ArrayList<Tuple>();
	}
	
	public String getName() {
		return schema.getName();
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder(schema.toString() + ":\n");
		
		for (Tuple tuple: tuples) {
			builder.append(' ');
			builder.append(tuple.toString());
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	public Tuple addTuple() {
		Tuple newTuple = new Tuple(schema);
		tuples.add(newTuple);
		schema.createFieldsForTuple(newTuple);
		return newTuple;
	}
	
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}
}
