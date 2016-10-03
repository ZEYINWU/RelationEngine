package zeyin.cis550.hw5.algebra;

public abstract class RelationalOperator implements IRelationalOperator {
	int processed = 0;
	String theName;
	
	@Override
	public String getName() {
		return theName;
	}

	@Override
	public int getTuplesProcessed() {
		return processed;
	}
	
	void incTuplesProcessed() {
		processed++;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = 
				new StringBuilder(getName() + 
						//"{" + getParameters() + "}" + 
						"[" + getTuplesProcessed() + "]");
		
		int childCount = getChildren().size();
		if (childCount == 0) {
			
		} else {
			builder.append('(');
			builder.append(getChildren().get(0).toString());
			
			for (int i = 1; i < childCount; i++) {
				builder.append(',');
				builder.append(getChildren().get(i).toString());
			}
			builder.append(')');
		}
		return builder.toString();
	}
}
