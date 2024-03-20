package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

public class ForwardProcessingEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(ForwardProcessor.class);

	private static final float MIN_BASAL_AREA = 0.001f;
	
	private ForwardProcessingState s;

	public ForwardProcessingEngine(GenusDefinitionMap genusDefinitionMap) {
		
		s = new ForwardProcessingState(genusDefinitionMap);
	}

	public void processPolygon(VdypPolygon polygon) throws ProcessingException {
		
		logger.info("Starting processing of polygon {}", polygon.getDescription());

		s.setStartingState(polygon);

		validatePolygon(polygon);
		
		s.setActive(LayerType.PRIMARY, 0);
	}

	private void validatePolygon(VdypPolygon polygon) throws ProcessingException {
		
		if (polygon.getDescription().getYear() < 1900) {
			
			throw new ProcessingException(MessageFormat.format("Polygon {}'s year value {} is < 1900", 101
					, polygon.getDescription().getName(), polygon.getDescription().getYear()));
		}
		
		// The following is extracted from BANKCHK1, simplified for the parameters METH_CHK = 4, 
		// LayerI = 1, and INSTANCE = 1. So IR = 1, which is the first bank, numbered 0.

		// => all that is done is that species with basal area < MIN_BASAL_AREA are removed.
		
		PolygonProcessingState a = s.getBank(LayerType.PRIMARY, 0);
		
		int lossCount = 0;
		for (int i = 1; i < a.nSpecies; i++) {
			if (a.basalArea[i][UtilizationClass.ALL.ordinal()] < MIN_BASAL_AREA) {
				lossCount += 1;
			} else if (lossCount > 0) { 
				a.replace(i - lossCount, i);
			}
		}
		
		if (lossCount > 0) {
			a.nSpecies -= lossCount;
			if (a.nSpecies == 0) {
				throw new ProcessingException(MessageFormat.format("Polygon {} layer 0 has no species with basal area above {}", 12
						, polygon.getDescription().getName(), MIN_BASAL_AREA));
			}
		}
	}
}
