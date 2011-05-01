/*
 * Copyright Author
 * (USE & RESTRICTIONS - Please read COPYRIGHT file)

 * Version : XX.XX
 * Date : 4/20/11 1:09 PM
 */

// Default Package
package DWLProject;

import java.util.Random;

import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.entity;

public class DataPartitioner_0_0 extends ViewableAtomic {
	private FlatFile flatFile;
	private CatFile aCatFile;
	private int[] catNumRecords, catDims, catSumLevels, catYears;
	private int MAX_DIMENSIONS = 3;
	private int MAX_SUMLEVELS = 1;
	private int errors;

	private static final String FF_IN = "FFin";
	private static final String CAT_FILE_OUT = "CatFileOut";
	private static final String ERROR_FILE = "errorFile";
	private static final String DP_DONE = "DPDone";
	private static final String DONE = "Done";

	// Phases
	private static final String PASSIVE = "passive";
	private static final String RECEIVE_FF = "ReceiveFF";
	private static final String CREATE_CAT = "CreateCATs";
	private static final String SEND_CAT = "SendCATs";
	private static final String CHECK_ERRORS = "CheckErrors";
	private static final String SEND_ERRORS = "SendErrorFile";
	private static final String ENDING = "Ending";

	private message doneMessage;
	private message catFileMessage;
	private message errorFileMessage;

	// Add Default Constructor
	public DataPartitioner_0_0() {
		this("DataPartitioner_0_0");
	}

	// Add Parameterized Constructors
	public DataPartitioner_0_0(String name) {
		super(name);
		// Structure information start
		// Add input port names
		addInport(FF_IN);

		// Add output port names
		addOutport(CAT_FILE_OUT);
		addOutport(DP_DONE);
		addOutport(ERROR_FILE);

		// add test input ports:

		// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);
		if (phaseIs(PASSIVE)) {
			for (int i = 0; i < x.size(); i++) {
				checkForFlatFile(x, i);
			}
		}
	}

	private void checkForFlatFile(message x, int i) {
		if (messageOnPort(x, FF_IN, i)) {
			entity value = x.getValOnPort(FF_IN, i);
			if (value instanceof FlatFile) {
				flatFile = (FlatFile) value;
				if (flatFile.getNumberOfCategories() < 0) {
					System.out.println("Invalid Flat File: # of Categories must be > 1");
					holdIn(PASSIVE, INFINITY);
				} else
					holdIn(RECEIVE_FF, flatFile.getRegistrationTime());
			} else {
				System.out.println("Not a Flat File: " + value.getName());
				holdIn(PASSIVE, INFINITY);
			}
		}
	}

	// Add internal transition function
	public void deltint() {
		if (phaseIs(RECEIVE_FF)) {
			holdIn(CREATE_CAT, 10);
		} else if (phaseIs(CREATE_CAT)) {
			Random rand = new Random();
			int recs = flatFile.getNumberOfRecords();
			int randDimsInCat;
			int randSumLevelsInCat;
			int cats = flatFile.getNumberOfCategories();
			errors = flatFile.getNumberOfErrors();
			int years = flatFile.getNumberOfYears();
			int randYearsInCat;
			int validRecs = recs - errors;
			int remainingRecords = validRecs;
			int totalRecsAssigned = 0;

			// Fill CAT arrays with random numbers when appropriate
			// int numberOfRecords;
			// int dimensions;
			// int summaryLevels;
			// Fill CAT Number of Records Array
			catNumRecords = new int[cats];
			for (int i = 0; i < cats; i++) {
				if (i == cats - 1 && remainingRecords > 0) // ensure the total
															// of records are
															// assigned
					catNumRecords[i] = remainingRecords;
				else
					catNumRecords[i] = rand.nextInt(validRecs + 1);
				totalRecsAssigned += catNumRecords[i];
				remainingRecords -= totalRecsAssigned;
			}
			;
			// Fill CAT Number of Dimensions Array (Any category may have from 1
			// to MAX_DIMENSIONS)
			catDims = new int[cats];
			for (int i = 0; i < cats; i++) {
				randDimsInCat = rand.nextInt(MAX_DIMENSIONS);
				catDims[i] = randDimsInCat + 1;
			}

			// Fill CAT Number of Summary Levels Array
			catSumLevels = new int[cats];
			for (int i = 0; i < cats; i++) {
				randSumLevelsInCat = rand.nextInt(MAX_SUMLEVELS);
				catSumLevels[i] = randSumLevelsInCat + 1;
			}

			// Fill CAT YEARS Array
			catYears = new int[cats];
			for (int i = 0; i < cats; i++) {
				randYearsInCat = rand.nextInt(years);
				catYears[i] = randYearsInCat + 1;
			}

			// Create the CAT Files
			catFileMessage = new message();

			for (int i = 0; i < cats; i++) {
				String catName = "CAT" + i;
				double regTime = rand.nextInt(5);
				regTime = Double.compare(regTime, 0D) == 1 ? regTime : 1D;
				aCatFile = new CatFile(catName, catNumRecords[i], regTime,
						catDims[i], catSumLevels[i], catYears[i]);
				catFileMessage.add(makeContent(CAT_FILE_OUT, aCatFile));
			}

			holdIn(SEND_CAT, 10);
		} else if (phaseIs(SEND_CAT)) {
			holdIn(CHECK_ERRORS, 10);
		} else if (phaseIs(CHECK_ERRORS)) {
			if (errors != 0) {
				ErrorFile ef = new ErrorFile(errors, 10D);
				errorFileMessage = new message();
				errorFileMessage.add(makeContent(ERROR_FILE, ef));
				holdIn(SEND_ERRORS, 10);
			} else {
				doneMessage = new message();
				doneMessage.add(makeContent(DP_DONE, new entity(DONE)));
				holdIn(ENDING, 1);
			}
		} else if (phaseIs(SEND_ERRORS)) {
			doneMessage = new message();
			doneMessage.add(makeContent(DP_DONE, new entity(DONE)));
			holdIn(ENDING, 1);
		} else
			passivate();
	}

	// Add confluent function
	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

	// Add output function
	public message out() {
		if (phaseIs(SEND_CAT))
			return catFileMessage;
		if (phaseIs(SEND_ERRORS))
			return errorFileMessage;
		if (phaseIs(ENDING))
			return doneMessage;
		return new message();
	}

	// Add Show State function
}
