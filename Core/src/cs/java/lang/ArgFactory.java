package cs.java.lang;

public interface ArgFactory<Type, Argument> {
	Type create(Argument argument);
}
