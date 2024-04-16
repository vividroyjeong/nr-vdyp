package ca.bc.gov.nrs.vdyp.common;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.model.GenusDefinition;

public class GenusDefinitionMap {
	private final Map<String, GenusDefinition> genusByAliasMap = new HashMap<>();
	private final Map<String, Integer> indexByAliasMap = new HashMap<>();
	private final int maxIndex;

	public GenusDefinitionMap(List<GenusDefinition> genusDefinitionList) {

		maxIndex = genusDefinitionList.size();

		int currentIndex = 1;
		for (GenusDefinition g : genusDefinitionList) {
			genusByAliasMap.put(g.getAlias(), g);
			indexByAliasMap.put(g.getAlias(), g.getPreference().orElse(currentIndex));

			currentIndex += 1;
		}
	}

	public GenusDefinition get(String alias) {
		GenusDefinition g = genusByAliasMap.get(alias);
		if (g == null) {
			throw new IllegalArgumentException(
					MessageFormat.format("Unable to find GenusDefinition for alias {0}", alias)
			);
		}
		return g;
	}

	public int getIndex(String alias) {
		Integer index = indexByAliasMap.get(alias);
		if (index == null) {
			throw new IllegalArgumentException(
					MessageFormat.format("Unable to find GenusDefinition for alias {0}", alias)
			);
		}
		return index;
	}

	public int getMaxIndex() {
		return maxIndex;
	}
}
