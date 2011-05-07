/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/23/11 4:09 PM
*/

// Default Package
package DWLProject;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.Pair;
import GenCol.entity;

/**
 * Transducer_0_0.java
 * 
 * @author Ivan C
 *
 */
public class Transducer_0_0 extends ViewableAtomic{

    private static final String COMMA = ", ";
	private static final String CARRIAGE_RETURN = "\r\n";
	private static final String MEASURES = "measures";
	private static final String ERROR_FILE = "errorFile";
	private static final String STATS = "stats";
	private static final String DP_DONE = "DPDone";
	private static final String HALT = "halt";

	//phases
	private static final String ACTIVE = "active";
	private static final String HALTING = "halting";
	
	private double clock;
	private Map<String, CatFile> catFilesArrived;
	private Map<String, CatFile> catFileCompleted;
	private Map<String, ExtCatFile> extCatFileArrived;
	private Map<String, ExtCatFile> extCatFileCompleted;
	private FlatFile flatFile = FlatFile.NULL;
	private ErrorFile errorFile = ErrorFile.NULL;
	private double totalCatTurnAround;
	private double totalExtCatTurnAround;
	private double totalCatProcessingTime;
	private double totalExtCatProcessingTime;
	private int totalRowsCat;
	private int totalRowsExtCat;
	private double observationTime;

    // Add Default Constructor
    public Transducer_0_0(){
        this("Transducer", 10D);
    }

    // Add Parameterized Constructors
    public Transducer_0_0(String name, double observationTime){
        super(name);
        this.observationTime = observationTime;
// Structure information start
        // Add input port names
        addInport(STATS);
        addInport(ERROR_FILE);

        // Add output port names
        addOutport(MEASURES);
//add test input ports:
        addTestInput(STATS, new FlatFile(100, 0, 5, 1, 2));
        addTestInput(ERROR_FILE, new ErrorFile(10, 1D));
        addTestInput(STATS, new CatFile("Cat1", 100, 1, 5, 1, 1, 1D));
        ExtCatFile anExtCatFile = new ExtCatFile("ExtCat1", 100, "L1", 2011, 1D, 1D);
        addTestInput(STATS, anExtCatFile);
        addTestInput(STATS, new CatFile("Cat2", 100, 1, 5, 2, 1, 1D));
        anExtCatFile = new ExtCatFile("ExtCat21", 100, "L1", 2011, 1D, 1D);
        addTestInput(STATS, anExtCatFile);
        anExtCatFile = new ExtCatFile("ExtCat22", 80, "L2", 2011, 1D, 1D);
        addTestInput(STATS, anExtCatFile);
    }

    // Add initialize function
    @Override
    public void initialize(){
		this.setBackgroundColor(Color.YELLOW);
        phase = ACTIVE;
        sigma = observationTime;
        clock = 0D;
        catFilesArrived = new HashMap<String, CatFile>();
        catFileCompleted = new HashMap<String, CatFile>();
        extCatFileArrived = new HashMap<String, ExtCatFile>();
        extCatFileCompleted = new HashMap<String, ExtCatFile>();
    	totalCatTurnAround = 0;
    	totalExtCatTurnAround = 0;
    	totalCatProcessingTime = 0D;
    	totalExtCatProcessingTime = 0D;
    	totalRowsCat = 0;
    	totalRowsExtCat = 0;
    }

    // Add external transition function
    @Override
    public void deltext(double e, message x){
    	clock += e;
    	Continue(e);
    	for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, STATS, i)) {
				entity val = x.getValOnPort(STATS, i);
				if (val instanceof FlatFile) {
					flatFile = (FlatFile) val;
					flatFile.setArrivalTime(clock);
				} else if (val instanceof Pair) {
					Pair aPair = (Pair) val;
					if (aPair.getValue() instanceof CatFile) {
						CatFile catFile = (CatFile) aPair.getValue();
						if (catFilesArrived.containsKey(catFile.getName())) {
							catFile.setCompletionTime(clock);
							catFileCompleted.put(catFile.getName(), catFile);
							totalCatTurnAround += clock
									- catFile.getArrivalTime();
							totalCatProcessingTime += catFile.getProcessingTime();
							totalRowsCat += catFile.getNumberOfRecords();
						} else {
							catFile.setArrivalTime(clock);
							catFilesArrived.put(catFile.getName(), catFile);
						}
					} else if (aPair.getValue() instanceof ExtCatFile) {
						ExtCatFile extCatFile = (ExtCatFile) aPair.getValue();
						if (extCatFileArrived.containsKey(extCatFile.getName())) {
							extCatFileCompleted.put(extCatFile.getName(), extCatFile);
							totalExtCatTurnAround += clock - extCatFile.getArrivalTime();
							totalExtCatProcessingTime += extCatFile.getProcessingTime();
							totalRowsExtCat += extCatFile.getNumberOfRecords();
						} else {
							extCatFile.setArrivalTime(clock);
							extCatFileArrived.put(extCatFile.getName(), extCatFile);
						}
						
					}
				} else if (val.getName().equals(DP_DONE)) {
					flatFile.setCompletionTime(clock);
				} else if (val.getName().equals(HALT)) {
					holdIn(HALTING, 1);
				}
			}
			if (messageOnPort(x, ERROR_FILE, i)) {
				entity val = x.getValOnPort(ERROR_FILE, i);
				if (val instanceof ErrorFile) {
					errorFile = (ErrorFile) val;
				}
			}
    	}
    	showCurrentState();
    }

    // Add internal transition function
    @Override
    public void deltint(){
    	clock += sigma;
    	passivate();
    	showCurrentState();
		this.setBackgroundColor(Color.GRAY);
    }

    // Add output function
    @Override
    public message out(){
    	StringBuilder myBuilder = new StringBuilder();
    	
    	message output = new message();
    	String totalLoadString = " - Total Load time: ";
		content aContent = makeContent(MEASURES, new entity(totalLoadString + clock));
		myBuilder.append(totalLoadString);
		myBuilder.append(COMMA);
		myBuilder.append(clock);
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);

    	String catFilesArrivedString = " - Cat files arrived: ";
		aContent = makeContent(MEASURES, new entity(catFilesArrivedString + catFilesArrived.size()));
		myBuilder.append(catFilesArrivedString);
		myBuilder.append(COMMA);
		myBuilder.append(catFilesArrived.size());
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);

    	String catFilesCompletedString = " - Cat files completed: ";
		aContent = makeContent(MEASURES, new entity(catFilesCompletedString + catFileCompleted.size()));
		myBuilder.append(catFilesCompletedString);
		myBuilder.append(COMMA);
		myBuilder.append(catFileCompleted.size());
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String extCatFilesArrivedString = " - ExtCat files arrived: ";
		aContent = makeContent(MEASURES, new entity(extCatFilesArrivedString + extCatFileArrived.size()));
		myBuilder.append(extCatFilesArrivedString);
		myBuilder.append(COMMA);
		myBuilder.append(extCatFileArrived.size());
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String extCatFilesCompletedString = " - ExtCat files completed: ";
		aContent = makeContent(MEASURES, new entity(extCatFilesCompletedString + extCatFileCompleted.size()));
		myBuilder.append(extCatFilesCompletedString);
		myBuilder.append(COMMA);
		myBuilder.append(extCatFileCompleted.size());
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String numberOfRowsString = " - # Rows: ";
		aContent = makeContent(MEASURES, new entity(numberOfRowsString + flatFile.getNumberOfRecords()));
		myBuilder.append(numberOfRowsString);
		myBuilder.append(COMMA);
		myBuilder.append(flatFile.getNumberOfRecords());
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String numberOfErrorsString = " - # Errors: ";
		aContent = makeContent(MEASURES, new entity(numberOfErrorsString + errorFile.getNumberOfRecords()));
		myBuilder.append(numberOfErrorsString);
		myBuilder.append(COMMA);
		myBuilder.append(errorFile.getNumberOfRecords());
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String avgTACatString = " - Avg Cat file turnaround time: ";
		double computeCatAvgTA = computeCatAvgTA();
		aContent = makeContent(MEASURES, new entity(avgTACatString + computeCatAvgTA));
		myBuilder.append(avgTACatString);
		myBuilder.append(COMMA);
		myBuilder.append(computeCatAvgTA);
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String avgExtTAString = " - Avg ExtCat file turnaround time: ";
		double computeExtCatAvgTA = computeExtCatAvgTA();
		aContent = makeContent(MEASURES, new entity(avgExtTAString + computeExtCatAvgTA));
		myBuilder.append(avgExtTAString);
		myBuilder.append(COMMA);
		myBuilder.append(computeExtCatAvgTA);
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String avgCatProcessingString = " - Avg Cat file processing time: ";
		double computeCatAvgPT = computeCatAvgPT();
		aContent = makeContent(MEASURES, new entity(avgCatProcessingString + computeCatAvgPT));
		myBuilder.append(avgCatProcessingString);
		myBuilder.append(COMMA);
		myBuilder.append(computeCatAvgPT);
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String avgExtCatProcessingString = " - Avg ExtCat file processing time: ";
		double computeExtCatAvgPT = computeExtCatAvgPT();
		aContent = makeContent(MEASURES, new entity(avgExtCatProcessingString + computeExtCatAvgPT));
		myBuilder.append(avgExtCatProcessingString);
		myBuilder.append(COMMA);
		myBuilder.append(computeExtCatAvgPT);
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String avgRowsPerCatString = " - Avg #Rows Per Cat file: ";
		int computeCatAvgRows = computeCatAvgRows();
		aContent = makeContent(MEASURES, new entity(avgRowsPerCatString + computeCatAvgRows));
		myBuilder.append(avgRowsPerCatString);
		myBuilder.append(COMMA);
		myBuilder.append(computeCatAvgRows);
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	String avgRowsPerExtCatString = " - Avg #Rows Per ExtCat file: ";
		int computeExtCatAvgRows = computeExtCatAvgRows();
		aContent = makeContent(MEASURES, new entity(avgRowsPerExtCatString + computeExtCatAvgRows));
		myBuilder.append(avgRowsPerExtCatString);
		myBuilder.append(COMMA);
		myBuilder.append(computeExtCatAvgRows);
		myBuilder.append(CARRIAGE_RETURN);
    	output.add(aContent);
    	
    	FileWriter aWriter = null;
    	try {
			aWriter = new FileWriter("DWLStats.cvs");
			aWriter.write(myBuilder.toString());
			aWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (aWriter != null) {
				try {
					aWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
    	
    	return output;
    }

    /**
     * Shows the current state
     */
    private void showCurrentState() {
		System.out.println("State of  "  +  name  +  ": " );
		System.out.println(" - Phase: " + phase);
		System.out.println(" - Sigma: " + sigma);
		displayCurrentStats();
	}
    
    private void displayCurrentStats() {
    	System.out.println(" - Total Load time: " + clock);
		System.out.println(" - Cat files arrived: " + catFilesArrived.size());
		System.out.println(" - Cat files completed: " + catFileCompleted.size());
		System.out.println(" - ExtCat files arrived: " + extCatFileArrived.size());
		System.out.println(" - ExtCat files completed: " + extCatFileCompleted.size());
		System.out.println(" - # Rows: " + flatFile.getNumberOfRecords());
		System.out.println(" - # Errors: "  + errorFile.getNumberOfRecords());
		System.out.println(" - Avg Cat file turnaround time: " + computeCatAvgTA());
		System.out.println(" - Avg ExtCat file turnaround time: " + computeExtCatAvgTA());
		System.out.println(" - Avg Cat file processing time: " + computeCatAvgPT());
		System.out.println(" - Avg ExtCat file processing time: " + computeExtCatAvgPT());
		System.out.println(" - Avg #Rows Per Cat file: " + computeCatAvgRows());
		System.out.println(" - Avg #Rows Per ExtCat file: " + computeExtCatAvgRows());
    }

	private double computeCatAvgTA() {
		double avgTA = 0D;
		if (!catFileCompleted.isEmpty()) {
			avgTA = totalCatTurnAround/catFileCompleted.size();
		}
		return avgTA;
	}

	private double computeExtCatAvgTA() {
		double avgTA = 0D;
		if (!extCatFileCompleted.isEmpty()) {
			avgTA = totalExtCatTurnAround/extCatFileCompleted.size();
		}
		return avgTA;
	}

	private double computeCatAvgPT() {
		double avgPT = 0D;
		if (!catFileCompleted.isEmpty()) {
			avgPT = totalCatProcessingTime/extCatFileCompleted.size();
		}
		return avgPT;
	}

	private double computeExtCatAvgPT() {
		double avgPT = 0D;
		if (!extCatFileCompleted.isEmpty()) {
			avgPT = totalExtCatProcessingTime/extCatFileCompleted.size();
		}
		return avgPT;
	}

	private int computeCatAvgRows() {
		int avgRows = 0;
		if (!catFileCompleted.isEmpty()) {
			avgRows = totalRowsCat/catFileCompleted.size();
		}
		return avgRows;
	}

	private int computeExtCatAvgRows() {
		int avgRows = 0;
		if (!extCatFileCompleted.isEmpty()) {
			avgRows = totalRowsExtCat/extCatFileCompleted.size();
		}
		return avgRows;
	}
}

