package zeyin.cis550.hw5.operators;

import java.util.ArrayList;
import java.util.List;

/**
 * Less-than test
 * @author zives
 *
 * @param <T> Type of the main parameter to be bound (typically a tuple)
 * @param <I> Type of the individual item to be compared (e.g., String, Integer)
 */
public class LessThan<T,I extends Comparable<I>> implements IExpression<Boolean,T> {
	IGetValue<T,I> field1;
	IGetValue<T,I> field2;
	List<IExpression<T, I>> children = new ArrayList<IExpression<T, I>>();
	
	public LessThan(IGetValue<T,I> arg1, IGetValue<T,I> arg2) {
		field1 = arg1;
		field2 = arg2;
	}

	@Override
	public Boolean getResult() {
		
		// Null is never equal to anything, including null
		if (field1.isNull() || field2.isNull())
			return false;
		
		return field1.getValue().compareTo(field2.getValue()) < 0;
	}

	@Override
	public List<IExpression<T, ?>> getChildExpressions() {
		return new ArrayList<IExpression<T,?>>();
	}

	@Override
	public void bind(List<T> inputValues) {
		field1.setSource(inputValues.get(0));
		field2.setSource(inputValues.get(1));
		
		for (int i = 0; i < children.size(); i++) {   
			IExpression<T, I> child = children.get(i);
			List<I> theInputs = new ArrayList<I>();
			theInputs.add(theInputs.get(i));
			child.bind(theInputs);
		}
	}
	
	@Override
	public String toString() {
		return field1.toString() + "=" + field2.toString();
	}
}
