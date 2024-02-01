package ca.bc.gov.nrs.vdyp.model;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import ca.bc.gov.nrs.vdyp.common.Computed;

public class VdypPolygon extends BaseVdypPolygon<VdypLayer, Float> {

	// TODO better name
	int grpBa1;

	public VdypPolygon(
			String polygonIdentifier, Float percentAvailable, String fiz, String becIdentifier,
			Optional<FipMode> modeFip
	) {
		super(polygonIdentifier, percentAvailable, fiz, becIdentifier, modeFip);
	}

	/**
	 * Copy constructs from the simple attributes of another polygon, but does not
	 * copy layers.
	 *
	 * @param <O>                     Type of the polygon to copy
	 * @param <U>                     Type of percent available in the other polygon
	 * @param toCopy                  The polygon to copy
	 * @param convertPercentAvailable Function to convert
	 */
	public <O extends BaseVdypPolygon<?, U>, U> VdypPolygon(O toCopy, Function<U, Float> convertPercentAvailable) {
		super(toCopy, convertPercentAvailable);
	}

	@Computed
	public int getInventoryTypeGroup() {
		return this.getLayers().get(LayerType.PRIMARY).getInventoryTypeGroup().orElseThrow(
				() -> new IllegalArgumentException("Inventory Type Group does not exist if there is no primary layer")
		);
	}

	@Computed
	public void setInventoryTypeGroup(int itg) {
		this.getLayers().get(LayerType.PRIMARY).setInventoryTypeGroup(Optional.of(itg));
	}

	// TODO better name
	@Computed
	public int getGrpBa1() {
		return this.getLayers().get(LayerType.PRIMARY).getEmpiricalRelationshipParameterIndex().orElseThrow(
				() -> new IllegalArgumentException(
						"Emperical Relationship Parameter Index does not exist if there is no primary layer"
				)
		);
	}

	// TODO better name
	@Computed
	public void setGrpBa1(int grpBa1) {
		this.getLayers().get(LayerType.PRIMARY).setEmpericalRelationshipParameterIndex(Optional.of(grpBa1));
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

		// TODO better name
		int grpBa1;

		@Override
		protected VdypPolygon doBuild() {
			return new VdypPolygon(
					polygonIdentifier.get(), percentAvailable.get(), forestInventoryZone.get(),
					biogeoclimaticZone.get(), modeFip
			);
		}

		@Override
		protected VdypLayer.Builder getLayerBuilder() {
			return new VdypLayer.Builder();
		}

	}
}
