package zeyin.cis550.hw5.schema;

public abstract class BasicAttribute<T> implements IAttribute<T> {
	/**
	 * Attribute name
	 */
	String name;
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName() + " " + getType().name() + "";
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
