package zeyin.cis550.hw5.algebra;

import java.util.ArrayList;
import java.util.List;

import zeyin.cis550.hw5.data.IField;
import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.schema.IAttribute;
import zeyin.cis550.hw5.schema.Schema;
import zeyin.cis550.hw5.schema.TypeException;

public class ProjectOperator extends RelationalOperator {
	IRelationalOperator input;
	Schema output;
	
	static int pid = 0;
	
	public ProjectOperator(List<String> fields, IRelationalOperator input) throws TypeException {
		this.input = input;
		
		theName = "Project" + pid++;
		output = new Schema(theName);
		for (String f: fields) {
			IAttribute<?> field = input.getOutputSchema().getAttribute(f);
			output.addAttribute(field);
			
			if (input.getOutputSchema().getKeys().contains(field))
				output.addKey(field);
		}
	}

	@Override
	public Schema getOutputSchema() {
		return output;
	}

	@Override
	public void initialize() {
		input.initialize();
	}

	@Override
	public Tuple getNext() {
		Tuple child = input.getNext();
		
		if (child == null)
			return null;
		else {
			incTuplesProcessed();
			Tuple result = new Tuple(output);
			for (int i = 0; i < output.getNumAttributes(); i++) {
				IField<?> inField = child.getField(output.getAttributeName(i));
				result.addField(inField);
			}
			return result;
		}
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
		return output.toString();
	}

}
