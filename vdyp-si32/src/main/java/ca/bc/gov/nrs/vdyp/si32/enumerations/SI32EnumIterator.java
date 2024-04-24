package ca.bc.gov.nrs.vdyp.si32.enumerations;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SI32EnumIterator<T extends Enum<T>> implements Iterator<T> {

	private final int lastIndex;
	private int currentIndex;
	private final T[] valueArray;

	public SI32EnumIterator(T[] values) {
		
		this(values, values[0], values[values.length - 1]);
	}

	public SI32EnumIterator(T[] values, T first, T last) {
		
		if (values == null) {
			throw new IllegalArgumentException("values parameter to SI32EnumIterator may not be null");
		}
		
		this.valueArray = values;
		this.currentIndex = first.ordinal();
		this.lastIndex = last.ordinal() + 1;
	}

	@Override
	public boolean hasNext() {
		return currentIndex < lastIndex;
	}

	@Override
	public T next() {
		if (currentIndex == lastIndex) {
			throw new NoSuchElementException();
		} else {
			T result = valueArray[currentIndex];
			
			currentIndex += 1;
			
			return result;
		}
	}
}
