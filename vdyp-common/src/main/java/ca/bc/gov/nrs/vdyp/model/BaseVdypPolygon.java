package ca.bc.gov.nrs.vdyp.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BaseVdypPolygon<L extends BaseVdypLayer<SP, SI>, PA, SP extends BaseVdypSpecies, SI extends BaseVdypSite> {

	private String polygonIdentifier; // FIP_P/POLYDESC
	private PA percentAvailable; // FIP_P2/PCTFLAND
	private Map<LayerType, L> layers = new LinkedHashMap<>();
	protected String biogeoclimaticZone;
	protected String forestInventoryZone;
	protected Optional<PolygonMode> modeFip;

	protected BaseVdypPolygon(
			String polygonIdentifier, PA percentAvailable, String fiz, String becIdentifier,
			Optional<PolygonMode> modeFip
	) {
		super();
		this.forestInventoryZone = fiz;
		this.biogeoclimaticZone = becIdentifier;
		this.modeFip = modeFip;
		this.polygonIdentifier = polygonIdentifier;
		this.percentAvailable = percentAvailable;
	}

	/**
	 * Copy constructs from the simple attributes of another polygon, but does not copy layers.
	 *
	 * @param <O>                     Type of the polygon to copy
	 * @param <U>                     Type of percent available in the other polygon
	 * @param toCopy                  The polygon to copy
	 * @param convertPercentAvailable Function to convert
	 */
	protected <O extends BaseVdypPolygon<?, U, ?, ?>, U> BaseVdypPolygon(
			O toCopy, Function<U, PA> convertPercentAvailable
	) {
		this(
				toCopy.getPolygonIdentifier(), convertPercentAvailable.apply(toCopy.getPercentAvailable()),
				toCopy.getForestInventoryZone(), toCopy.getBiogeoclimaticZone(), toCopy.getModeFip()
		);
	}

	public String getPolygonIdentifier() {
		return polygonIdentifier;
	}

	public void setPolygonIdentifier(String polygonIdentifier) {
		this.polygonIdentifier = polygonIdentifier;
	}

	public Map<LayerType, L> getLayers() {
		return layers;
	}

	public void setLayers(Map<LayerType, L> layers) {
		this.layers = layers;
	}

	public void setLayers(Collection<L> layers) {
		this.layers = new EnumMap<>(LayerType.class);
		layers.forEach(spec -> this.layers.put(spec.getLayer(), spec));
	}

	public PA getPercentAvailable() {
		return percentAvailable;
	}

	public void setPercentAvailable(PA percentAvailable) {
		this.percentAvailable = percentAvailable;
	}

	public String getBiogeoclimaticZone() {
		return biogeoclimaticZone;
	}

	public void setBiogeoclimaticZone(String biogeoclimaticZone) {
		this.biogeoclimaticZone = biogeoclimaticZone;
	}

	public String getForestInventoryZone() {
		return forestInventoryZone;
	}

	public void setForestInventoryZone(String forestInventoryZone) {
		this.forestInventoryZone = forestInventoryZone;
	}

	public Optional<PolygonMode> getModeFip() {
		return modeFip;
	}

	public void setModeFip(Optional<PolygonMode> modeFip) {
		this.modeFip = modeFip;
	}

	protected abstract static class Builder< //
			T extends BaseVdypPolygon<L, PA, SP, SI>, //
			L extends BaseVdypLayer<SP, SI>, //
			PA, //
			SP extends BaseVdypSpecies, //
			SI extends BaseVdypSite, //
			LB extends BaseVdypLayer.Builder<L, SP, SI, SPB, SIB>, //
			SPB extends BaseVdypSpecies.Builder<SP>, //
			SIB extends BaseVdypSite.Builder<SI>> //

			extends ModelClassBuilder<T> {
		protected Optional<String> polygonIdentifier = Optional.empty();
		protected Optional<PA> percentAvailable = Optional.empty();
		protected Optional<String> biogeoclimaticZone = Optional.empty();
		protected Optional<String> forestInventoryZone = Optional.empty();
		protected Optional<PolygonMode> modeFip = Optional.empty();

		protected List<L> layers = new LinkedList<>();

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> polygonIdentifier(String polygonIdentifier) {
			this.polygonIdentifier = Optional.of(polygonIdentifier);
			return this;
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> percentAvailable(PA pa) {
			this.percentAvailable = Optional.of(pa);
			return this;
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> biogeoclimaticZone(String biogeoclimaticZone) {
			this.biogeoclimaticZone = Optional.of(biogeoclimaticZone);
			return this;
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> forestInventoryZone(String forestInventoryZone) {
			this.forestInventoryZone = Optional.of(forestInventoryZone);
			return this;
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> modeFip(Optional<PolygonMode> modeFip) {
			this.modeFip = modeFip;
			return this;
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> modeFip(PolygonMode modeFip) {
			return modeFip(Optional.of(modeFip));
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> addLayer(L layer) {
			this.layers.add(layer);
			return this;
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> addLayers(Collection<L> layers) {
			this.layers.addAll(layers);
			return this;
		}

		public Builder<T, L, PA, SP, SI, LB, SPB, SIB> buildLayer(Consumer<LB> specConfig) {
			var layerBuilder = getLayerBuilder();
			layerBuilder.polygonIdentifier(this.polygonIdentifier.get());
			specConfig.accept(layerBuilder);
			this.layers.add(layerBuilder.build());
			return this;
		}

		protected abstract LB getLayerBuilder();

		public <PA2> Builder<T, L, PA, SP, SI, LB, SPB, SIB>
				copy(BaseVdypPolygon<?, PA2, ?, ?> toCopy, Function<PA2, PA> paConvert) {
			polygonIdentifier(toCopy.getPolygonIdentifier());
			percentAvailable(paConvert.apply(toCopy.getPercentAvailable()));
			biogeoclimaticZone(toCopy.getBiogeoclimaticZone());
			forestInventoryZone(toCopy.getForestInventoryZone());
			return this;
		}

		@Override
		protected void check(Collection<String> errors) {
			requirePresent(polygonIdentifier, "polygonIdentifier", errors);
			requirePresent(percentAvailable, "percentAvailable", errors);
			requirePresent(biogeoclimaticZone, "biogeoclimaticZone", errors);
			requirePresent(forestInventoryZone, "forestInventoryZone", errors);
		}

		@Override
		protected void postProcess(T result) {
			super.postProcess(result);

			// Add species
			for (L layer : layers) {
				result.getLayers().put(layer.getLayer(), layer);
			}
		}

	}
}
