package ca.bc.gov.nrs.vdyp.model;

import java.util.function.Consumer;

public class VdypPolygon extends BaseVdypPolygon<VdypLayer, Float> {

	public VdypPolygon(String polygonIdentifier, Float percentAvailable) {
		super(polygonIdentifier, percentAvailable);
	}

	/**
	 * Accepts a configuration function that accepts a builder to configure.
	 *
	 * <pre>
	 * VdypPolygon myPolygon = VdypPolygon.build(builder-&gt; {
			builder.polygonIdentifier(polygonId);
			builder.percentAvailable(percentAvailable);
	 * })
	 * </pre>
	 *
	 * @param config The configuration function
	 * @return The object built by the configured builder.
	 * @throws IllegalStateException if any required properties have not been set by
	 *                               the configuration function.
	 */
	public static VdypPolygon build(Consumer<Builder> config) {
		var builder = new Builder();
		config.accept(builder);
		return builder.build();
	}

	public static class Builder extends BaseVdypPolygon.Builder<VdypPolygon, VdypLayer, Float> {

		@Override
		protected VdypPolygon doBuild() {
			return (new VdypPolygon(
					polygonIdentifier.get(), //
					percentAvailable.get()
			));
		}

		@Override
		protected VdypLayer.Builder getLayerBuilder() {
			return new VdypLayer.Builder();
		}

	}
}
