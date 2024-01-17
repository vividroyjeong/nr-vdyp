package ca.bc.gov.nrs.vdyp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesDistributionSet
{
	private Map<String, SpeciesDistribution> speciesDistribution = new HashMap<>();
	
	public SpeciesDistributionSet(List<SpeciesDistribution> speciesDistributionList)
	{
		for (SpeciesDistribution sd: speciesDistributionList) 
		{
			speciesDistribution.put(sd.getSpecies(), sd);
		}
	}
	
	Float getSpeciesDistribution(String species)
	{
		if (speciesDistribution.containsKey(species))
			return speciesDistribution.get(species).getPercentage();
		else
			return 0.0f;
	}
}
