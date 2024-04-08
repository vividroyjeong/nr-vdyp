package ca.bc.gov.nrs.vdyp.si32.enumerations;

import java.util.Iterator;

public abstract class SI32EnumIterator<T extends Enum<T>> implements Iterator<T> {

	private final T last;
	private T index;
	private final T[] valueArray;

	protected SI32EnumIterator(T first, T last, T[] values) {
		this.index = first;
		this.last = last;
		
		this.valueArray = values;
	}

	@Override
	public boolean hasNext() {
		return !index.equals(last);
	}

	@Override
	public T next() {
		T result = index;
		index = valueArray[index.ordinal() + 1];
		return result;
	}
}
