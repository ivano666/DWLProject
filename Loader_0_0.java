/*
 *			Copyright Author
 * (USE & RESTRICTIONS - Please read COPYRIGHT file)

 * Version		: XX.XX
 * Date		: 4/20/11 1:09 PM
 */

// Default Package
package DWLProject;

import java.util.ArrayList;
import java.util.List;

import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Loader_0_0 extends ViewableAtomic {
	private static final message NULL_MESSAGE = new message();

	private static final String SUMMARY_LEVEL_PREFIX = "L";
	//Output ports
	private static final String INSERT = "insert";
	private static final String DONE = "done";
	
	//Input ports
	private static final String CAT_IN = "CatIn";
	
	//Phases
	private static final String PASSIVE = "passive";
	private static final String BUSY = "busy";
	private static final String SENDING = "sending";
	
	private CatFile currentCatFile;
	private message outputMessage;
	
	// Add Default Constructor
	public Loader_0_0() {
		this("Loader_0_0");
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param name
	 */
	public Loader_0_0(String name) {
		super(name);
		// Structure information start
		// Add input port names
		addInport(CAT_IN);

		// Add output port names
		addOutport(DONE);
		addOutport(INSERT);

		// add test input ports:
        addTestInput(CAT_IN, new CatFile("Cat1", 10, 10, 3, 1, 2011));

		// Structure information end
		initialize();
	}

	@Override
	public void initialize() {
		super.initialize();
		phase = PASSIVE;
		sigma = INFINITY;
		outputMessage = null;
		currentCatFile = null;
	}

	@Override
	public void deltext(double e, message x) {
		Continue(e);
		if(phaseIs(PASSIVE)) {
			for (int i = 0; i < x.size(); i++) {
				checkForCatFile(x, i);
			}
		}
	}

	/**
     * Checks if a <code>CatFile</code> is in port <code>CAT_FILE_IN</code>
     * and stores it in an internal queue that is used later on to provide work to
     * the different <tt>loaders</tt>
     * 
     * @param x
     * @param i
     */
    private void checkForCatFile(message x, int i) {
		if (messageOnPort(x, CAT_IN, i)) {
			entity value = x.getValOnPort(CAT_IN, i);
			if (value instanceof CatFile) {
				currentCatFile = (CatFile) value;
				holdIn(BUSY, currentCatFile.getRegistrationTime());
			}
			else {
				System.out.println("Not a Cat File: " + value.getName());
				holdIn(PASSIVE, INFINITY);
			}
		}
	}
	
	@Override
	public void deltint() {
		if (phaseIs(SENDING)){
			passivateIn(PASSIVE);
		}
		if(phaseIs(BUSY)) {
			holdIn(SENDING, 0);
			List<ExtCatFile> theFiles = createExtCatFiles();
			prepareOutput(theFiles);
		}
	}

	/**
	 * Builds the output message
	 * 
	 * @param theFiles
	 */
	private void prepareOutput(List<ExtCatFile> theFiles) {
		outputMessage = new message();
		for (ExtCatFile aFile : theFiles) {
			outputMessage.add(makeContent(INSERT, aFile));
		}
		outputMessage.add(makeContent(DONE, new entity(DONE)));
	}

	/**
	 * Creates the output <code>ExtCatFile</code> files
	 * 
	 * @return
	 */
	private List<ExtCatFile> createExtCatFiles() {
		List<ExtCatFile> theFiles = new ArrayList<ExtCatFile>(currentCatFile.getNumberOfSummaryLevels()); 
		for (int i = 1; i <= currentCatFile.getNumberOfSummaryLevels(); i++) {
			String summaryLevel = SUMMARY_LEVEL_PREFIX+i;
			String name = currentCatFile.getName()+summaryLevel+currentCatFile.getYear();
			ExtCatFile aFile = new ExtCatFile(name, 
					currentCatFile.getNumberOfRecords(), summaryLevel, currentCatFile.getYear());
			theFiles.add(aFile);
		}
		return theFiles;
	}

	@Override
	public void deltcon(double e, message x) {
		deltint();
		deltext(0D, x);
	}

	@Override
	public message out() {
		if(phaseIs(SENDING)) {
			return outputMessage;
		}	
		return NULL_MESSAGE;
	}

	/**
	 * 
	 * @return <code>CAT_IN</code> port
	 */
	public static String getCatIn() {
		return CAT_IN;
	}

	/**
	 * 
	 * @return <code>DONE</code> port
	 */
	public static String getDone() {
		return DONE;
	}

	/**
	 * @return <code>INSERT</code> port
	 */
	public static String getInsert() {
		return INSERT;
	}

	// Add Show State function
}
