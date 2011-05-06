/*
 *			Copyright Author
 * (USE & RESTRICTIONS - Please read COPYRIGHT file)

 * Version		: XX.XX
 * Date		: 4/20/11 1:09 PM
 */

// Default Package
package DWLProject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.Pair;
import GenCol.entity;

public class Loader_0_0 extends ViewableAtomic {
	private static final String EMPTY_STRING = " ";

	private static final message NULL_MESSAGE = new message();

	private static final String SUMMARY_LEVEL_PREFIX = "L";
	//Output ports
	private static final String EXT_CAT_OUT = "extcatout";
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
		this("Loader");
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
		addOutport(EXT_CAT_OUT);

		// add test input ports:
		CatFile someFile = new CatFile("Cat1", 10, 10, 3, 1, 1);
		Pair aPair = new Pair(getName(), someFile);
        addTestInput(CAT_IN, aPair);
		someFile = new CatFile("Cat2", 10, 10, 3, 1, 2);
		aPair = new Pair(getName(), someFile);
		addTestInput(CAT_IN, aPair);

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
			if (value instanceof Pair) {
				Pair aPair = (Pair) value;
				if (aPair.getKey().equals(this.getName())) {
					if (aPair.getValue() instanceof CatFile) {
						currentCatFile = (CatFile) aPair.getValue();
						holdIn(BUSY, currentCatFile.getTimeToRegister());
						this.setBackgroundColor(Color.ORANGE);
					}
					else {
						System.out.println("Not a Cat File: " + value.getName());
						holdIn(PASSIVE, INFINITY);
						this.setBackgroundColor(Color.GRAY);
					}
				}
			}
		}
	}
	
	@Override
	public void deltint() {
		if (phaseIs(SENDING)){
			passivateIn(PASSIVE);
			this.setBackgroundColor(Color.GRAY);
		}
		if(phaseIs(BUSY)) {
			holdIn(SENDING, 0);
			this.setBackgroundColor(Color.ORANGE);
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
			outputMessage.add(makeContent(EXT_CAT_OUT, aFile));
		}
		Pair thePair = new Pair(this, currentCatFile);
		content theContent = makeContent(DONE, thePair);
		outputMessage.add(theContent);
	}

	/**
	 * Creates the output <code>ExtCatFile</code> files
	 * 
	 * @return
	 */
	private List<ExtCatFile> createExtCatFiles() {
		Random rand = new Random();
		List<ExtCatFile> theFiles = new ArrayList<ExtCatFile>(currentCatFile.getNumberOfSummaryLevels()); 
		for (int i = 1; i <= currentCatFile.getNumberOfSummaryLevels(); i++) {
			String summaryLevel = SUMMARY_LEVEL_PREFIX+i;
			int[] years = getYears(currentCatFile.getYears());
			for (int j = 0; j < years.length; j++) {
				String name = currentCatFile.getName()+summaryLevel+years[j];
				double regTime = rand.nextInt(5);
				ExtCatFile aFile = new ExtCatFile(name, 
						currentCatFile.getNumberOfRecords()/i, summaryLevel, years[j], 1D, regTime);
				aFile.setParentCatFile(currentCatFile);
				theFiles.add(aFile);
				currentCatFile.addExtCatFile(aFile);
			}
		}
		return theFiles;
	}

	/**
	 * Builds the array of years based on the number of years. 
	 * The assumption is to start with today's year and go
	 * backwards.
	 * 
	 * @param numberOfYears
	 * @return
	 */
	private int[] getYears(int numberOfYears) {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int[] yearsArray = null;
		if (numberOfYears > 0) {
			yearsArray = new int[numberOfYears];
			for (int i = 0; i < yearsArray.length; i++) {
				yearsArray[i] = currentYear - i;
			}
		}
		return yearsArray;
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
	public static String getExtCatOut() {
		return EXT_CAT_OUT;
	}

	@Override
	public void showState() {
		super.showState();
		System.out.println("The CatFile is: " + currentCatFile != null ? currentCatFile : EMPTY_STRING);
	}
	
	@Override
	public String getTooltipText() {
		StringBuilder myBuilder = new StringBuilder();
		myBuilder.append("\n");
		myBuilder.append("Cat File: ");
		myBuilder.append(currentCatFile != null ? currentCatFile : EMPTY_STRING);
		return super.getTooltipText() + myBuilder.toString();
	}
}
