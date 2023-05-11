package ca.bc.gov.nrs.vdyp.test;

import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class VydpMatchers {
	
	public static <T> Matcher<String> parseAs(Matcher<? super T> parsedMatcher, Function<String, T> parser) {
		return new BaseMatcher<String>() {

			@Override
			public boolean matches(Object actual) {
				return parsedMatcher.matches(parser.apply(actual.toString()));
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("parses as ");
				parsedMatcher.describeTo(description);
			}
			
		};
	}
	
}
