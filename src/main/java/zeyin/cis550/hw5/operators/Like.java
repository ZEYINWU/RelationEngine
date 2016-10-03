package zeyin.cis550.hw5.operators;

import java.util.ArrayList;
import java.util.List;

/**
 * LIKE binary predicate: matches field1's value against field2's value,
 * assuming that field2 contains a standard regex.  (Note that this is NOT
 * the syntax of SQL LIKE.)
 * 
 * @author zives
 *
 * @param <T> Main arg type
 * @param <I> Value type
 */
public class Like<T,I extends Comparable<I>> implements IExpression<Boolean,T> {
	IGetValue<T,I> field1;
	IGetValue<T,I> field2;
	List<IExpression<T, I>> children = new ArrayList<IExpression<T, I>>();
	
	public Like(IGetValue<T,I> arg1, IGetValue<T,I> arg2) {
		field1 = arg1;
		field2 = arg2;
	}

	@Override
	public Boolean getResult() {
		return field1.getValue().toString().matches(field2.getValue().toString());
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
