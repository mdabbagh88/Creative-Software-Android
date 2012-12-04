package cs.java.lang;

public interface Field<T> {
	T get();

	String key();

	void set(T value);
}
