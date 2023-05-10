package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class LineParser {
	
	List<LineParserSegment> segments = new ArrayList<>();
	Charset charset = StandardCharsets.US_ASCII;
	
	static abstract class LineParserSegment {
		int length;
		
		public int getLength() {
			return length;
		}
		
		public LineParserSegment(int length) {
			super();
			this.length = length;
		}

		public abstract void parseIntoMap(String toParse, Map<String, Object> map) throws ValueParseException;
	}
		
	static class LineParserNullSegment extends LineParserSegment {
		
		public LineParserNullSegment(int length) {
			super(length);
		}

		@Override
		public void parseIntoMap(String toParse, Map<String, Object> map) {
			// do nothing
		}
	}
	
	static abstract class LineParserValueSegment<T> extends LineParserSegment  {
		String name;
		abstract T parse(String value) throws ValueParseException;
		
		public String getName() {
			return name;
		}
		
		@Override	
		public void parseIntoMap(String toParse, Map<String, Object> map) throws ValueParseException {
			var value = this.parse(toParse);
			map.put(this.getName(), value);
		}

		public LineParserValueSegment(int length, String name) {
			super(length);
			this.name = name;
		}
	}
	
	public LineParser space(int length) {
		segments.add(new LineParserNullSegment(length));
		return this;
	}
	
	public LineParser integer(int length, String name) {
		segments.add(new LineParserValueSegment<Integer>(length, name) {
			
			@Override
			Integer parse(String value) throws ValueParseException {
				String stripped = value.strip();
				try {
					return Integer.parseInt(stripped);
				} catch (NumberFormatException ex) {
					throw new ValueParseException(stripped, ex);
				}
			}
			
		});
		return this;
	}
	
	public LineParser floating(int length, String name) {
		segments.add(new LineParserValueSegment<Float>(length, name) {
			
			@Override
			Float parse(String value) throws ValueParseException {
				String stripped = value.strip();
				try {
					return Float.parseFloat(stripped);
				} catch (NumberFormatException ex) {
					throw new ValueParseException(stripped, ex);
				}
			}
			
		});
		return this;
	}
	
	public LineParser string(int length, String name) {
		segments.add(new LineParserValueSegment<String>(length, name) {
			
			@Override
			String parse(String value) throws ValueParseException {
				return value.strip();
			}
			
		});
		return this;
	}
	
	public LineParser string(String name) {
		segments.add(new LineParserValueSegment<String>(-1, name) {
			
			@Override
			String parse(String value) throws ValueParseException {
				return value.strip();
			}
			
		});
		return this;
	}
	
	public <T> LineParser parse(int length, String name, ValueParser<T> parser) {
		segments.add(new LineParserValueSegment<T>(length, name) {
			
			@Override
			T parse(String value) throws ValueParseException {
				return parser.parse(value);
			}
			
		});
		return this;
	}
	
	public <T> LineParser parse(String name, ValueParser<T> parser) {
		segments.add(new LineParserValueSegment<T>(-1, name) {
			
			@Override
			T parse(String value) throws ValueParseException {
				return parser.parse(value);
			}
			
		});
		return this;
	}

	List<String> segmentize(String line) {
		List<String> result = new ArrayList<>(segments.size());
		
		int i = 0;
		for(var segment: segments) {
			if(i>=line.length()) {
				result.add(null);
				continue;
			};
			String segmentString;
			if(segment.getLength()>=0 && i+segment.getLength()<line.length()) {
				segmentString = line.substring(i, i+segment.length);
			} else {
				segmentString = line.substring(i);
			}
			i+=segmentString.length();
			result.add(segmentString);
		}
		
		return result;
	}
	
	public Map<String,Object> parse(String line) throws ValueParseException {
		var segments = segmentize(line);
		return parse(segments);
	}

	private Map<String,Object> parse(List<String> segmentStrings) throws ValueParseException {
		if(segmentStrings.size() != segments.size()) {
			throw new IllegalStateException("segment strings and segment halders must have the same size");
		}
		
		var result = new HashMap<String, Object>();
		
		for(int i=0; i<segments.size(); i++) {
			var segmentHandler = segments.get(i);
			var segmentString = segmentStrings.get(i);
			if(segmentString !=null) {
				segmentHandler.parseIntoMap(segmentString, result);
			}
		}
		
		return result;
	}
	
	public <T> T parse (InputStream is, T result, BiFunction<Map<String, Object>, T, T> addToResult) throws IOException, ResourceParseException {
		var reader = new BufferedReader(new InputStreamReader(is, charset));
		String line;
		int lineNumber = 0;
		while((line = reader.readLine())!=null) {
			lineNumber++;
			try {
				if(isStopLine(line)) {
					break;
				}
				var segments = segmentize(line);
				if(isStopSegment(segments)) {
					break;
				}
				var entry = parse(segments);
				if(isStopEntry(entry)) {
					break;
				}
				result = addToResult.apply(entry, result);
			} catch (ValueParseException ex) {
				throw new ResourceParseException(lineNumber, ex);
			}
		}
		return result;
	}
	
	public List<Map<String, Object>> parse (InputStream is) throws IOException, ResourceParseException {
		var result = new ArrayList<Map<String, Object>> ();
		result = this.parse(is, result, (v, r)->{r.add(v); return r;});
		return result;
	}
	
	public boolean isStopEntry(Map<String, Object> entry) {
		return false;
	}
	
	public boolean isStopSegment(List<String> entry) {
		return false;
	}

	public boolean isStopLine(String line) {
		return false;
	}
}
