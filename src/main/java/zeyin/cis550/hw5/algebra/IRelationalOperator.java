package zeyin.cis550.hw5.algebra;

import java.util.List;

import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.schema.Schema;

public interface IRelationalOperator {
	/**
	 * The operator's internal unique name
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Number of tuples we've processed
	 * @return
	 */
	public int getTuplesProcessed();
	
	/**
	 * Child (input) operators
	 * 
	 * @return
	 */
	public List<IRelationalOperator> getChildren();
	
	/**
	 * Parameters
	 */
	public String getParameters();
	
	/**
	 * Schema of the output
	 * @return
	 */
	public Schema getOutputSchema();
	
	/**
	 * Initialize (open) the operator - only call once
	 */
	public void initialize();
	
	/**
	 * Reset the iterator for the stream -- can be called multiple
	 * times in contrast to initialize()
	 */
	public void resetIterator();
	
	/**
	 * Get a tuple from the iterator
	 * @return
	 */
	public Tuple getNext();
	
	/**
	 * Shut down
	 */
	public void shutdown();
}
