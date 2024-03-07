package ca.bc.gov.nrs.vdyp.vri;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.ValueOrMarker;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;
import ca.bc.gov.nrs.vdyp.io.EndOfRecord;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.LineParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.AbstractStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.GroupingStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ControlledValueParser;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
import ca.bc.gov.nrs.vdyp.model.LayerType;

public class VriLayerParser
		implements ControlMapValueReplacer<StreamingParserFactory<Map<LayerType, VriLayer>>, String> {

	static final String LAYER = "LAYER"; // LAYER
	static final String CROWN_CLOSURE = "CROWN_CLOSURE"; // CC
	static final String BASE_AREA = "BASE_AREA"; // BA
	static final String TREES_PER_HECTARE = "TREES_PER_HECTARE"; // TPH
	static final String UTILIZATION = "UTILIZATION"; // UTL

	@Override
	public ControlKey getControlKey() {
		return ControlKey.VRI_YIELD_LAYER_INPUT;
	}

	@Override
	public StreamingParserFactory<Map<LayerType, VriLayer>>
			map(String fileName, FileResolver fileResolver, Map<String, Object> control)
					throws IOException, ResourceParseException {
		return () -> {
			var lineParser = new LineParser() //
					.strippedString(25, VriPolygonParser.POLYGON_IDENTIFIER) //
					.space(1) //
					.value(
							1, LAYER,
							ValueParser.valueOrMarker(
									ValueParser.LAYER,
									ValueParser.optionalSingleton("Z"::equals, EndOfRecord.END_OF_RECORD)
							)
					) //
					.floating(6, CROWN_CLOSURE) //
					.floating(9, BASE_AREA) //
					.space(1) //
					.floating(8, TREES_PER_HECTARE) //
					.space(1) //
					.floating(4, UTILIZATION);

			var is = fileResolver.resolveForInput(fileName);

			var delegateStream = new AbstractStreamingParser<ValueOrMarker<Optional<VriLayer>, EndOfRecord>>(
					is, lineParser, control
			) {

				@SuppressWarnings({ "unchecked" })
				@Override
				protected ValueOrMarker<Optional<VriLayer>, EndOfRecord> convert(Map<String, Object> entry) {
					var polygonId = (String) entry.get(VriPolygonParser.POLYGON_IDENTIFIER);
					var layer = (ValueOrMarker<Optional<LayerType>, EndOfRecord>) entry.get(LAYER);
					var crownClosure = (Float) entry.get(CROWN_CLOSURE);
					var baseArea = (Float) entry.get(BASE_AREA);
					var treesPerHectare = (Float) entry.get(TREES_PER_HECTARE);
					var utilization = (Float) entry.get(UTILIZATION);

					var vmBuilder = new ValueOrMarker.Builder<Optional<VriLayer>, EndOfRecord>();
					return layer.handle(l -> {
						VriLayer fipLayer = VriLayer.build(layerBuilder -> {
							layerBuilder.polygonIdentifier(polygonId);
							layerBuilder.layerType(l.get());

							layerBuilder.crownClosure(crownClosure);
							layerBuilder.baseArea(baseArea);
							layerBuilder.treesPerHectare(treesPerHectare);
							layerBuilder.utilization(utilization);
						});

						return vmBuilder.value(Optional.of(fipLayer));

					}, vmBuilder::marker);
				}

			};

			return new GroupingStreamingParser<Map<LayerType, VriLayer>, ValueOrMarker<Optional<VriLayer>, EndOfRecord>>(
					delegateStream
			) {

				@Override
				protected boolean skip(ValueOrMarker<Optional<VriLayer>, EndOfRecord> nextChild) {
					return nextChild.getValue().map(x -> x.map(layer -> {
						return false;
					}).orElse(true)) // If the layer is not present (Unknown layer type) ignore
							.orElse(false); // If it's a marker, let it through so the stop method can see it.
				}

				@Override
				protected boolean stop(ValueOrMarker<Optional<VriLayer>, EndOfRecord> nextChild) {
					return nextChild.isMarker();
				}

				@Override
				protected Map<LayerType, VriLayer>
						convert(List<ValueOrMarker<Optional<VriLayer>, EndOfRecord>> children) {
					return children.stream().map(ValueOrMarker::getValue).map(Optional::get) // Should never be empty as
							// we've filtered out
							// markers
							.flatMap(Optional::stream) // Skip if empty (and unknown layer type)
							.collect(Collectors.toMap(VriLayer::getLayer, x -> x));
				}

			};
		};
	}

	@Override
	public ValueParser<Object> getValueParser() {
		return FILENAME;
	}
}
