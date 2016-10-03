package zeyin.cis550.hw5.algebra;

import java.util.ArrayList;
import java.util.List;

import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.operators.IExpression;
import zeyin.cis550.hw5.schema.Schema;
import zeyin.cis550.hw5.schema.TypeException;

public class NestedLoopsJoinOperator extends RelationalOperator {
	IRelationalOperator outer;
	IRelationalOperator inner;
	IExpression<Boolean,Tuple> predicate;
	
	Tuple lastLeft = null;
	
	static int joinInx = 0;
	
	Schema newSchema;
	
	public NestedLoopsJoinOperator(IExpression<Boolean,Tuple> predicate, 
			IRelationalOperator outer, IRelationalOperator inner) throws TypeException {
		this.outer = outer;
		this.inner = inner;
		this.predicate = predicate;
		
		theName = "Join" + joinInx++;
		newSchema = new Schema(theName);
		
		// Cartesian product of attributes
		for (int i = 0; i < outer.getOutputSchema().getNumAttributes(); i++) {
			newSchema.addAttribute(outer.getOutputSchema().getAttribute(i));
		}
		for (int i = 0; i < inner.getOutputSchema().getNumAttributes(); i++) {
			newSchema.addAttribute(inner.getOutputSchema().getAttribute(i));
		}

		// Cartesian product of keys too!
		for (int i = 0; i < outer.getOutputSchema().getNumKeys(); i++) {
			newSchema.addKey(outer.getOutputSchema().getKey(i));
		}
		for (int i = 0; i < inner.getOutputSchema().getNumKeys(); i++) {
			newSchema.addKey(inner.getOutputSchema().getKey(i));
		}
	}

	@Override
	public Schema getOutputSchema() {
		return newSchema;
	}
	
	@Override
	public void initialize() {
		outer.initialize();
		inner.initialize();
	}

	@Override
	public Tuple getNext() {
		Tuple left = lastLeft;
		
		// Nothing to join from the outer relation
		// Typically means this is our first call
		if (left == null) {
			left = outer.getNext();
			lastLeft = left;
		}
		
		// If there's still nothing, then we have nothing left
		if (left == null) {
			return null;
		}
		
		List<Tuple> inputs = new ArrayList<Tuple>();
		do {
		
			Tuple right = inner.getNext();
			
			// If we get end of loop...
			if (right == null) {
				left = outer.getNext();
				lastLeft = left;
				if (left == null)
					return null;
				else {
					inner.resetIterator();
					// And fall through to the next iteration...
				}
			} else {
				// Bind & eval predicate
				inputs.clear();
				inputs.add(left);
				inputs.add(right);
				predicate.bind(inputs);
				
				// Does it satisfy the join condition?  If so,
				// create an output tuple
				if (predicate.getResult()) {
					incTuplesProcessed();
					
					Tuple newTuple = new Tuple(newSchema);
					
					// Cartesian product of fields
					for (int i = 0; i < left.getNumFields(); i++) {
						newTuple.addField(left.getField(i));
					}
					for (int i = 0; i < right.getNumFields(); i++) {
						newTuple.addField(right.getField(i));
					}
					
					return newTuple;
				}
			}
		} while (true);
	}

	@Override
	public void shutdown() {
		outer.shutdown();
		inner.shutdown();
	}

	@Override
	public void resetIterator() {
		outer.resetIterator();
		inner.resetIterator();
	}

	@Override
	public List<IRelationalOperator> getChildren() {
		List<IRelationalOperator> ret = new ArrayList<IRelationalOperator>();
		
		ret.add(outer);
		ret.add(inner);
		return ret;
	}

	@Override
	public String getParameters() {
		return predicate.toString();
	}

}

