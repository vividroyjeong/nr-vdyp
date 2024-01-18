package ca.bc.gov.nrs.vdyp.fip.model;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import ca.bc.gov.nrs.vdyp.model.BaseVdypPolygon;

public class FipPolygon extends BaseVdypPolygon<FipLayer, Optional<Float>> {

	private String forestInventoryZone; // FIP_P/FIZ
	private String biogeoclimaticZone; // FIP_P/BEC
	private Optional<FipMode> modeFip; // FIP_P2/MODE / MODEfip
	private Optional<String> nonproductiveDescription; // FIP_P3/NPDESC
	private float yieldFactor; // FIP_P4/YLDFACT

	public FipPolygon(
			String polygonIdentifier, String fiz, String becIdentifier, Optional<Float> percentAvailable,
			Optional<FipMode> modeFip, Optional<String> nonproductiveDescription, float yieldFactor
	) {
		super(polygonIdentifier, percentAvailable);
		this.forestInventoryZone = fiz;
		this.biogeoclimaticZone = becIdentifier;
		this.modeFip = modeFip;
		this.nonproductiveDescription = nonproductiveDescription;
		this.yieldFactor = yieldFactor;
	}

	public String getForestInventoryZone() {
		return forestInventoryZone;
	}

	public void setForestInventoryZone(String forestInventoryZone) {
		this.forestInventoryZone = forestInventoryZone;
	}

	public String getBiogeoclimaticZone() {
		return biogeoclimaticZone;
	}

	public void setBiogeoclimaticZone(String biogeoclimaticZone) {
		this.biogeoclimaticZone = biogeoclimaticZone;
	}

	public Optional<FipMode> getModeFip() {
		return modeFip;
	}

	public void setModeFip(Optional<FipMode> modeFip) {
		this.modeFip = modeFip;
	}

	public Optional<String> getNonproductiveDescription() {
		return nonproductiveDescription;
	}

	public void setNonproductiveDescription(Optional<String> nonproductiveDescription) {
		this.nonproductiveDescription = nonproductiveDescription;
	}

	public float getYieldFactor() {
		return yieldFactor;
	}

	public void setYieldFactor(float yieldFactor) {
		this.yieldFactor = yieldFactor;
	}

	/**
	 * Accepts a configuration function that accepts a builder to configure.
	 *
	 * <pre>
	 * VdypPolygon myPolygon = VdypPolygon.build(builder-&gt; {
			builder.polygonIdentifier(polygonId);
			builder.percentAvailable(percentAvailable);
			builder.forestInventoryZone
			builder.biogeoclimaticZone
			builder.
	 * })
	 * </pre>
	 *
	 * @param config The configuration function
	 * @return The object built by the configured builder.
	 * @throws IllegalStateException if any required properties have not been set by
	 *                               the configuration function.
	 */
	public static FipPolygon build(Consumer<Builder> config) {
		var builder = new Builder();
		config.accept(builder);
		return builder.build();
	}

	public static class Builder extends BaseVdypPolygon.Builder<FipPolygon, FipLayer, Optional<Float>> {
		private Optional<String> forestInventoryZone;
		private Optional<String> biogeoclimaticZone;
		private Optional<FipMode> modeFip;
		private Optional<String> nonproductiveDescription;
		private Optional<Float> yieldFactor;

		public Builder() {
			this.percentAvailable(Optional.empty());
		}

		public Builder forestInventoryZone(String forestInventoryZone) {
			this.forestInventoryZone = Optional.of(forestInventoryZone);
			return this;
		}

		public Builder biogeoclimaticZone(String biogeoclimaticZone) {
			this.biogeoclimaticZone = Optional.of(biogeoclimaticZone);
			return this;
		}

		public Builder modeFip(Optional<FipMode> modeFip) {
			this.modeFip = modeFip;
			return this;
		}

		public Builder nonproductiveDescription(Optional<String> nonproductiveDescription) {
			this.nonproductiveDescription = nonproductiveDescription;
			return this;
		}

		public Builder modeFip(FipMode modeFip) {
			modeFip(Optional.of(modeFip));
			return this;
		}

		public Builder nonproductiveDescription(String nonproductiveDescription) {
			nonproductiveDescription(Optional.of(nonproductiveDescription));
			return this;
		}

		public Builder yieldFactor(Float yieldFactor) {
			this.yieldFactor = Optional.of(yieldFactor);
			return this;
		}

		public Builder percentAvailable(Float percentAvailable) {
			percentAvailable(Optional.of(percentAvailable));
			return this;
		}

		@Override
		protected void check(Collection<String> errors) {
			super.check(errors);
			requirePresent(forestInventoryZone, "forestInventoryZone", errors);
			requirePresent(biogeoclimaticZone, "biogeoclimaticZone", errors);
			requirePresent(yieldFactor, "yieldFactor", errors);
		}

		@Override
		protected FipPolygon doBuild() {
			return (new FipPolygon(
					polygonIdentifier.get(), //
					forestInventoryZone.get(), //
					biogeoclimaticZone.get(), //
					percentAvailable.get(), //
					modeFip, //
					nonproductiveDescription, //
					yieldFactor.get() //
			));
		}

		@Override
		protected FipLayer.Builder getLayerBuilder() {
			return new FipLayer.Builder();
		}

	}
}
