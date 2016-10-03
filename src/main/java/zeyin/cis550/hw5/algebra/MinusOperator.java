package zeyin.cis550.hw5.algebra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import zeyin.cis550.hw5.data.IField;
import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.schema.Schema;

public class MinusOperator extends RelationalOperator {
	Map<List<IField<?>>, Tuple> set;
	IRelationalOperator minuend; 
	IRelationalOperator subtrahend;
	
	Iterator<Tuple> resultIter = null;
	
	static int mid = 0;
	
	public MinusOperator(IRelationalOperator minuend, IRelationalOperator subtrahend) {
		this.minuend = minuend;
		this.subtrahend = subtrahend;
		
		set = new HashMap<List<IField<?>>, Tuple>();
		
		theName = "Minus" + mid++;
	}

	@Override
	public List<IRelationalOperator> getChildren() {
		List<IRelationalOperator> ret = new ArrayList<IRelationalOperator>();
		
		ret.add(minuend);
		ret.add(subtrahend);
		return ret;
	}


	@Override
	public String getParameters() {
		return "";
	}

	@Override
	public Schema getOutputSchema() {
		return minuend.getOutputSchema();
	}

	@Override
	public void initialize() {
		minuend.initialize();
		subtrahend.initialize();
	}

	@Override
	public void resetIterator() {
		minuend.resetIterator();
		subtrahend.resetIterator();
		
		resultIter = null;
	}

	@Override
	public Tuple getNext() {
		if (resultIter == null) {
			Tuple min = minuend.getNext();
			while (min != null) {
				set.put(min.getKey(), min);
				min = minuend.getNext();
			}
			
			Tuple sub = subtrahend.getNext();
			while (sub != null) {
				set.remove(sub.getKey());
				sub = subtrahend.getNext();
			}
			
			resultIter = set.values().iterator();
		}		
		
		if (resultIter.hasNext()) {
			incTuplesProcessed();
			return resultIter.next();
		} else
			return null;
	}

	@Override
	public void shutdown() {
		minuend.shutdown();
		subtrahend.shutdown();
	}

}
