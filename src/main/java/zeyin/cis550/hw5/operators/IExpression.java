package zeyin.cis550.hw5.operators;

import java.util.List;

public interface IExpression<T,I> {

	public T getResult();
	
	public List<IExpression<I,?>> getChildExpressions();
	
	public void bind(List<I> inputValues);
}
