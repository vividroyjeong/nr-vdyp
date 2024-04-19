package ca.bc.gov.nrs.vdyp.common;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.model.GenusDefinition;

public class GenusDefinitionMap {
	private final Map<String, GenusDefinition> genusByAliasMap = new HashMap<>();
	private final Map<String, Integer> indexByAliasMap = new HashMap<>();
	private final Map<Integer, GenusDefinition> genusByIndexMap = new HashMap<>();
	private int maxIndex;

	public GenusDefinitionMap(List<GenusDefinition> genusDefinitionList) {

		maxIndex = genusDefinitionList.size();

		int currentIndex = 1;
		for (GenusDefinition g : genusDefinitionList) {
			genusByAliasMap.put(g.getAlias(), g);
			indexByAliasMap.put(g.getAlias(), g.getPreference().orElse(currentIndex));
			genusByIndexMap.put(g.getPreference().orElse(currentIndex), g);

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

	public GenusDefinition getByIndex(int index) {
		GenusDefinition g = genusByIndexMap.get(index);
		if (g == null) {
			throw new IllegalArgumentException(
					MessageFormat.format("Unable to find GenusDefinition for index {0}", index)
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
	
	public void removeGenus(String alias) {
		if (! indexByAliasMap.containsKey(alias)) {
			throw new IllegalArgumentException(MessageFormat.format("GenusDefinitionMap does not contain alias {}", alias));
		}
		
		genusByIndexMap.remove(indexByAliasMap.get(alias));
		indexByAliasMap.remove(alias);
		genusByAliasMap.remove(alias);
		
		maxIndex -= 1;
	}
	
	public void removeGenus(int index) {
		removeGenus(genusByIndexMap.get(index).getAlias());
	}

	public int getMaxIndex() {
		return maxIndex;
	}
}
