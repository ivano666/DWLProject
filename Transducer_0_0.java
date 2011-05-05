/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/23/11 4:09 PM
*/

// Default Package
package DWLProject;

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

    private static final String MEASURES = "measures";
	private static final String ERROR_FILE = "errorFile";
	private static final String STATS = "stats";
	private static final String ACTIVE = "active";
	private static final Object DP_DONE = "DPDone";
	
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

    // Add Default Constructor
    public Transducer_0_0(){
        this("Transducer_0_0", 10D);
    }

    // Add Parameterized Constructors
    public Transducer_0_0(String name, double observationTime){
        super(name);
// Structure information start
        // Add input port names
        addInport(STATS);
        addInport(ERROR_FILE);

        // Add output port names
        addOutport(MEASURES);
//add test input ports:
        addTestInput(STATS, new FlatFile(100, 0, 5, 1, 2));
        addTestInput(ERROR_FILE, new ErrorFile(10, 1D));
        addTestInput(STATS, new CatFile("Cat1", 100, 1, 5, 1, 1));
        ExtCatFile anExtCatFile = new ExtCatFile("ExtCat1", 100, "L1", 2011, 1D, 1D);
        addTestInput(STATS, anExtCatFile);
        addTestInput(STATS, new CatFile("Cat2", 100, 1, 5, 2, 1));
        anExtCatFile = new ExtCatFile("ExtCat21", 100, "L1", 2011, 1D, 1D);
        addTestInput(STATS, anExtCatFile);
        anExtCatFile = new ExtCatFile("ExtCat22", 80, "L2", 2011, 1D, 1D);
        addTestInput(STATS, anExtCatFile);
    }

    // Add initialize function
    @Override
    public void initialize(){
        phase = ACTIVE;
        sigma = INFINITY;
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
							totalCatProcessingTime += catFile.getOriginalTimeToRegister();
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
							extCatFileArrived.put(extCatFile.getName(), extCatFile);
						}
						
					}
				} else if (val instanceof ExtCatFile) {
						ExtCatFile extCatFile = (ExtCatFile) val;
						if (extCatFileArrived.containsKey(extCatFile.getName())) {
							extCatFileCompleted.put(extCatFile.getName(), extCatFile);
							totalExtCatTurnAround += clock - extCatFile.getArrivalTime();
							totalExtCatProcessingTime += extCatFile.getProcessingTime();
							totalRowsExtCat += extCatFile.getNumberOfRecords();
						} else {
							extCatFileArrived.put(extCatFile.getName(), extCatFile);
						}
				} else if (val.getName().equals(DP_DONE)) {
					flatFile.setCompletionTime(clock);
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
    }

    // Add output function
    @Override
    public message out(){
    	message output = new message();
    	content aContent = makeContent(MEASURES, new Pair(" - Cat files arrived: ", catFilesArrived.size()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - Cat files completed: ", catFileCompleted.size()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - ExtCat files arrived: ", extCatFileArrived.size()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - ExtCat files completed: ", extCatFileCompleted.size()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - # Rows: ", flatFile.getNumberOfRecords()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - # Errors: ", errorFile.getNumberOfRecords()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - Avg Cat file turnaround time: ", computeCatAvgTA()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - Avg ExtCat file turnaround time: ", computeExtCatAvgTA()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - Avg Cat file processing time: ", computeCatAvgPT()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - Avg ExtCat file processing time: ", computeExtCatAvgPT()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - Avg #Rows Per Cat file: ", computeCatAvgRows()));
    	output.add(aContent);
    	aContent = makeContent(MEASURES, new Pair(" - Avg #Rows Per ExtCat file: ", computeExtCatAvgRows()));
    	output.add(aContent);
    	
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

