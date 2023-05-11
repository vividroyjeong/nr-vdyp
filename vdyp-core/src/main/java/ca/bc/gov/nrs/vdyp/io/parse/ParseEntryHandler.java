package ca.bc.gov.nrs.vdyp.io.parse;

@FunctionalInterface
public interface ParseEntryHandler<Entry, Result> {
	public Result addTo(Entry entry, Result result) throws ValueParseException;
}
