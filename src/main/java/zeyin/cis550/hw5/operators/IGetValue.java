package zeyin.cis550.hw5.operators;

public interface IGetValue<T, I extends Comparable<I>> {
	public void setSource(T source);
	
	public boolean isNull();
	
	public I getValue();
}
