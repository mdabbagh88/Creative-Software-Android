package cs.java.collections;

import static cs.java.lang.Lang.unsuported;

import java.util.Iterator;

public abstract class GenericIterator<T> implements Iteration<T> {
	private int index = -1;
	private boolean iteratingForward = true;
	private int iterationLength;

	public GenericIterator(int length) {
		this.iterationLength = length;
	}

	protected abstract T getValue();

	@Override
	public boolean hasNext() {
		if (iteratingForward) return index < iterationLength - 1;
		return index > 0;
	}

	@Override
	public int index() {
		return index;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public T next() {
		if (iteratingForward)
			++index;
		else --index;
		return getValue();
	}

	protected void onRemove() {
		throw unsuported();
	}

	@Override
	public final void remove() {
		onRemove();
		index--;
		iterationLength--;
	}

	@Override
	public Iteration<T> reverse() {
		this.iteratingForward = false;
		index = iterationLength;
		return this;
	}

	@Override
	public Iteration<T> skip(int length) {
		if (iteratingForward)
			index += length;
		else index -= length;
		return this;
	}
}