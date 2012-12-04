package cs.java.lang;

public interface Service {
	<T> T get(Class<T> service);

	<T> void serve(Class<T> class1, T service);
}
