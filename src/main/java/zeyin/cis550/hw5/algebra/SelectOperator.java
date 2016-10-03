package zeyin.cis550.hw5.algebra;

import java.util.ArrayList;
import java.util.List;

import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.operators.IExpression;
import zeyin.cis550.hw5.schema.Schema;

public class SelectOperator extends RelationalOperator {
	IRelationalOperator input;
	IExpression<Boolean,Tuple> predicate;
	
	static int sid = 0;
	
	public SelectOperator(IExpression<Boolean,Tuple> predicate, IRelationalOperator input) {
		this.input = input;
		this.predicate = predicate;
		
		theName = "Select" + sid++;
	}

	@Override
	public Schema getOutputSchema() {
		return input.getOutputSchema();
	}

	@Override
	public void initialize() {
		input.initialize();
	}

	
	
	@Override
	public Tuple getNext() {
		Tuple child = input.getNext();
		List<Tuple> inputs = new ArrayList<Tuple>();
		inputs.add(child);
		inputs.add(child);

		while (child != null) {
			inputs.set(0, child);
			inputs.set(1, child);
			predicate.bind(inputs);
			if (predicate.getResult())
				break;

			child = input.getNext();
		}
		if (child != null)
			incTuplesProcessed();
		return child;
	}

	@Override
	public void shutdown() {
		input.shutdown();
	}

	@Override
	public void resetIterator() {
		input.resetIterator();
	}

	@Override
	public List<IRelationalOperator> getChildren() {
		List<IRelationalOperator> ret = new ArrayList<IRelationalOperator>();
		ret.add(input);
		return ret;
	}

	@Override
	public String getParameters() {
		return predicate.toString();
	}

}
