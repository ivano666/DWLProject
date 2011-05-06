/*
 *			Copyright Author
 * (USE & RESTRICTIONS - Please read COPYRIGHT file)

 * Version		: XX.XX
 * Date		: 4/20/11 1:09 PM
 */

// Default Package
package DWLProject;

import java.awt.Color;

import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;
import DWLProject.utils.ExtCatFileStatus;
import GenCol.Pair;
import GenCol.entity;

/**
 * Writer_0_0.java
 * 
 * @author Ivan C
 *
 */
public class Writer_0_0 extends ViewableAtomic {
	private static final String EMPTY_STRING = " ";

	private static final message NULL_MESSAGE = new message();

	//Output ports
	private static final String EXT_CAT_OUT = "ExtCatOut";
	private static final String DONE = "done";
	
	//Input ports
	private static final String EXT_CAT_IN = "ExtCatIn";
	
	//Phases
	private static final String PASSIVE = "passive";
	private static final String BUSY = "busy";
	
	private ExtCatFile currentExtCatFile;
	private message outputMessage;
	
	// Add Default Constructor
	public Writer_0_0() {
		this("Writer");
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param name
	 */
	public Writer_0_0(String name) {
		super(name);
		// Structure information start
		// Add input port names
		addInport(EXT_CAT_IN);

		// Add output port names
		addOutport(DONE);
		addOutport(EXT_CAT_OUT);

		// add test input ports:
		ExtCatFile someFile = new ExtCatFile("ExtCat1",100, "L1", 2011, 10, 1D);
		Pair aPair = new Pair(getName(), someFile);
        addTestInput(EXT_CAT_IN, aPair);
		someFile = new ExtCatFile("ExtCat2", 150, "L1", 2011, 15, 1D);
		aPair = new Pair(getName(), someFile);
		addTestInput(EXT_CAT_IN, aPair);

		// Structure information end
		initialize();
	}

	@Override
	public void initialize() {
		super.initialize();
		phase = PASSIVE;
		sigma = INFINITY;
		outputMessage = null;
		currentExtCatFile = null;
	}

	@Override
	public void deltext(double e, message x) {
		Continue(e);
		if(phaseIs(PASSIVE)) {
			for (int i = 0; i < x.size(); i++) {
				checkForExtCatFile(x, i);
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
    private void checkForExtCatFile(message x, int i) {
		if (messageOnPort(x, EXT_CAT_IN, i)) {
			entity value = x.getValOnPort(EXT_CAT_IN, i);
			if (value instanceof Pair) {
				Pair aPair = (Pair) value;
				if (aPair.getKey().equals(this.getName())) {
					if (aPair.getValue() instanceof ExtCatFile) {
						currentExtCatFile = (ExtCatFile) aPair.getValue();
						holdIn(BUSY, currentExtCatFile.getProcessingTime());
						currentExtCatFile.setStatus(ExtCatFileStatus.INPROGRESS);
						this.setBackgroundColor(Color.ORANGE);
					}
					else {
						System.out.println("Not an Ext Cat File: " + value.getName());
						holdIn(PASSIVE, INFINITY);
						this.setBackgroundColor(Color.GRAY);
					}
				}
			}
		}
	}
	
	@Override
	public void deltint() {
		if (phaseIs(DONE)) {
			passivateIn(PASSIVE);
			this.setBackgroundColor(Color.GRAY);
		}
		if (phaseIs(BUSY)) {
			currentExtCatFile.setStatus(ExtCatFileStatus.COMPLETED);
			currentExtCatFile.getParentCatFile().addProcessedExtCatFile(currentExtCatFile);
			prepareOutput();
			holdIn(DONE, 0);
			this.setBackgroundColor(Color.ORANGE);
		}
	}

	/**
	 * Prepares the output
	 */
	private void prepareOutput() {
		outputMessage = new message();
		outputMessage.add(makeContent(EXT_CAT_OUT, currentExtCatFile));
		Pair thePair = new Pair(this, currentExtCatFile);
		content theContent = makeContent(DONE, thePair);
		outputMessage.add(theContent);
	}

	@Override
	public void deltcon(double e, message x) {
		deltint();
		deltext(0D, x);
	}

	@Override
	public message out() {
		if (phaseIs(DONE)) {
			return outputMessage;
		}
		return NULL_MESSAGE;
	}

	/**
	 * 
	 * @return <code>EXT_CAT_IN</code> port
	 */
	public static String getExtCatIn() {
		return EXT_CAT_IN;
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
		System.out.println("The ExtCatFile is: " + currentExtCatFile != null ? currentExtCatFile : EMPTY_STRING);
	}
	
	@Override
	public String getTooltipText() {
		StringBuilder myBuilder = new StringBuilder();
		myBuilder.append("\n");
		myBuilder.append("Ext Cat File: ");
		myBuilder.append(currentExtCatFile != null ? currentExtCatFile : EMPTY_STRING);
		return super.getTooltipText() + myBuilder.toString();
	}
}
