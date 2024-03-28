package ca.bc.gov.nrs.vdyp.si32;

import java.util.Iterator;
import java.util.Map;

public abstract class SI32EnumIterator<T extends SI32Enum<T>> implements Iterator<T> {

	private final T last;
	private T index;
	private final Map<Integer, T> enumByIndexMap;

	protected SI32EnumIterator(T first, T last, Map<Integer, T> enumByIndexMap) {
		this.index = first;
		this.last = last;
		
		this.enumByIndexMap = enumByIndexMap;
	}

	@Override
	public boolean hasNext() {
		return !index.equals(last);
	}

	@Override
	public T next() {
		index = enumByIndexMap.get(index.getValue() + 1);
		return index;
	}
}
