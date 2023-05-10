package ca.bc.gov.nrs.vdyp.io.parse;

@FunctionalInterface
public interface ValueParser<T> {
	T parse(String string) throws ValueParseException;
}
