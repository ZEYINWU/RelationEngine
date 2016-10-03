package zeyin.cis550.hw5.algebra;

import java.util.ArrayList;
import java.util.List;

import zeyin.cis550.hw5.data.DoubleField;
import zeyin.cis550.hw5.data.IField;
import zeyin.cis550.hw5.data.IntField;
import zeyin.cis550.hw5.data.StringField;
import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.schema.DoubleAttribute;
import zeyin.cis550.hw5.schema.IAttribute;
import zeyin.cis550.hw5.schema.IntAttribute;
import zeyin.cis550.hw5.schema.Schema;
import zeyin.cis550.hw5.schema.StringAttribute;
import zeyin.cis550.hw5.schema.TypeException;

public class RenameOperator extends RelationalOperator {
	IRelationalOperator input;
	Schema output;
	
	static int rid = 0;
	
	public RenameOperator(List<String> inFields, List<String> newFields,
			IRelationalOperator input) throws TypeException {
		this.input = input;
		
		if (inFields.size() != newFields.size())
			throw new TypeException("Rename must have same number of input and renamed fields!");
		
		if (input.getOutputSchema().getNumAttributes() != inFields.size())
			throw new TypeException("Rename must map all the fields!");
		
		theName = "Rename" + rid++;
		output = new Schema(theName);
		for (int i = 0; i < inFields.size(); i++) {
			IAttribute<?> inAtt = input.getOutputSchema().getAttribute(inFields.get(i));
			IAttribute<?> outAtt = null;
			switch (inAtt.getType()) {
			case INT:
				outAtt = new IntAttribute(newFields.get(i));
				break;
			case DOUBLE:
				outAtt = new DoubleAttribute(newFields.get(i));
				break;
			case STRING:
				outAtt = new StringAttribute(newFields.get(i));
				break;
			default:
				throw new TypeException("Rename does not know about attribute type " + 
						inAtt.getType().name());
			}
			output.addAttribute(outAtt);
			if (input.getOutputSchema().getKeys().contains(inAtt))
				output.addKey(outAtt);
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

	@SuppressWarnings("unchecked")
	@Override
	public Tuple getNext() {
		Tuple child = input.getNext();
		
		if (child == null)
			return null;
		else {
			incTuplesProcessed();
			Tuple result = new Tuple(output);
			for (int i = 0; i < output.getNumAttributes(); i++) {
				IField<?> inField = child.getField(i);
				IField<?> outAtt = null;
				switch (inField.getAttribute().getType()) {
				case INT:
					outAtt = new IntField((IAttribute<Integer>) output.getAttribute(i), 
							(Integer)inField.getValue());
					break;
				case DOUBLE:
					outAtt =  new DoubleField((IAttribute<Double>) output.getAttribute(i), 
							(Double)inField.getValue());
					break;
				case STRING:
					outAtt =  new StringField((IAttribute<String>) output.getAttribute(i), 
							(String)inField.getValue());
					break;
				}
				result.addField(outAtt);
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
		return input.getOutputSchema() + " -> " + output;
	}

}
