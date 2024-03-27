package ca.bc.gov.nrs.vdyp.common;

public class RuntimeWrapperException extends RuntimeException {

	private static final long serialVersionUID = -1041354494252224860L;

	public RuntimeWrapperException(Class<? extends Exception> klazz, Exception ex) {
		super(ex);
		this.klazz = klazz;
	}

	Class<? extends Exception> klazz;

	<T extends Exception> void throwAs(Class<T> returnKlazz) throws T {
		if (returnKlazz.isAssignableFrom(this.klazz)) {
			throw returnKlazz.cast(getCause());
		}
	}

	IllegalStateException fail() {
		return new IllegalStateException(this);
	}

}
