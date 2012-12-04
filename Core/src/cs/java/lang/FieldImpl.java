package cs.java.lang;

public class FieldImpl<T> implements Field<T> {

	private final String key;
	private T value;

	public FieldImpl(String key) {
		this.key = key;
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public void set(T value) {
		this.value = value;
	}

}
