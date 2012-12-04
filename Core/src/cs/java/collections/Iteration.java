package cs.java.collections;

import java.util.Iterator;

public interface Iteration<T> extends Iterator<T>, Iterable<T> {
	int index();

	Iteration<T> reverse();

	Iteration<T> skip(int length);
}