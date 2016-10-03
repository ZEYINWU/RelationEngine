package zeyin.cis550.hw5.algebra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zeyin.cis550.hw5.data.Relation;
import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.schema.Schema;

public class TableScanOperator extends RelationalOperator {
	Relation input;
	Iterator<Tuple> iter;
	
	static int pid = 0;
	
	public TableScanOperator(Relation input) {
		this.input = input;
		
		theName = input.getName() + pid++;
	}

	@Override
	public Schema getOutputSchema() {
		return input.getSchema();
	}

	@Override
	public void initialize() {
		iter = input.iterator();
	}

//	public boolean hasNext(){
//		return iter.hasNext();
//	}
	
	@Override
	public Tuple getNext() {
		if (!iter.hasNext())
			return null;
		
		Tuple child = iter.next();
		incTuplesProcessed();
		return child;
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void resetIterator() {
		iter = input.iterator();
	}

	@Override
	public List<IRelationalOperator> getChildren() {
		List<IRelationalOperator> ret = new ArrayList<IRelationalOperator>();
		return ret;
	}

	@Override
	public String getParameters() {
		return input.getName();
	}

}
