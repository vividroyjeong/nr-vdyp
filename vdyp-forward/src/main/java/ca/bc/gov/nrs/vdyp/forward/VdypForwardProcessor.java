package ca.bc.gov.nrs.vdyp.forward;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.application.VDYPApplication;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.FileSystemFileResolver;
import ca.bc.gov.nrs.vdyp.io.FileSystemRelativeFileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.ResourceParseException;

/**
 * 
 *                  The algorithmic part of VDYP7 GROWTH Program.
 *                  In October, 2000 this was split off from the main PROGRAM,
 *                  which now just defines units and fills /C_CNTR/
 *
 *    VDYPPASS    IN/OUT    I*4(10)   Major Control Functions
 *      (1)       IN          Perform Initiation activities? (0=No, 1=Yes)
 *      (2)       IN          Open the stand data files (0=No, 1=Yes)
 *      (3)       IN          Process stands (0=No, 1=Yes)
 *      (4)       IN          Allow multiple polygons (0=No, 1=Yes)
 *                             (Subset of stand processing. May limit to 1 stand)
 *      (5)       IN          CLOSE data files.
 *      (10)      OUT         Indicator variable that in the case of single stand processing
 *                            with VDYPPASS(4) set, behaves as follows:
 *                            -100  due to EOF, nothing to read
 *                             other -ve value, incl -99.  Could not process the stand.
 *                            0   Stand was processed and written
 *                            +ve value.  Serious error. Set to IER.
 *    IER         OUTPUT    I*4       Error code
 *                                     0: No error
 *                                    >0: Error
 *                                    99: Error generated in routine called by this subr.
 *                                    <0: Warning
 *    HISTORY
 *    05DEC01   VDYPPASS(1-5) used to control all activites. (usually all=1)
 *    13JAN02   Set IYRSTART in common /YEARS/  (Same as IYRFIRST in VGROWYR)
 *    14JAN02   Optionally call SITEADDU
 *    25JAN02   Make OPENING separate from initialization
 *    25JAN02   Set output variable VDYPPASS(10) for single-stand processing
 *
 * @author Michael Junkin, Vivid Solutions
 */
public class VdypForwardProcessor
{
	private static final Logger log = LoggerFactory.getLogger(VdypForwardProcessor.class);

	private final VdypForwardApplication app;
	private final Set<VdypPass> vdypPassSet;
	
	private Map<String, Object> controlMap = Collections.emptyMap();
	
	public VdypForwardProcessor(VdypForwardApplication app, Set<VdypPass> vdypPassSet, List<String> controlFileNames) 
			throws IOException, ResourceParseException, ProcessingException {
		
		this.app = app;
		this.vdypPassSet = vdypPassSet;
		
		log.info("VDYPPASS: " + vdypPassSet);
        log.debug("VDYPPASS(1): Perform Initiation activities?");
        log.debug("VDYPPASS(2): Open the stand data files");
        log.debug("VDYPPASS(3): Process stands");
        log.debug("VDYPPASS(4): Allow multiple polygons");
        log.debug("VDYPPASS(5): Close data files");
        log.debug(" ");
		
		init(new FileSystemFileResolver(), controlFileNames);
		
		process();
	}

	/**
	 * Initialize VDYPForwardStart
	 *
	 * @param resolver
	 * @param controlFileNames
	 * @throws IOException
	 * @throws ResourceParseException
	 */
	void init(FileResolver resolver, List<String> controlFileNames) 
			throws IOException, ResourceParseException {

		// Load the control map

		var parser = new VdypForwardControlParser(app);
		
		for (var controlFileName: controlFileNames)
		{
			log.info("Resolving and parsing {}", controlFileName);
			
			try (var is = resolver.resolve(controlFileName)) {
				Path controlFilePath = Path.of(controlFileName).getParent();
				FileSystemRelativeFileResolver relativeResolver = new FileSystemRelativeFileResolver(controlFilePath.toString());
				
				controlMap = parser.parse(is, relativeResolver);
			}
		}
	}

	/**
	 * Implements VDYP_SUB
	 * 
	 * @throws ProcessingException
	 */
	public void process() throws ProcessingException {
	}
}
