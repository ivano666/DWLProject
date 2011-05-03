/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/20/11 1:09 PM
*/

// Default Package
package DWLProject;

import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.Queue;
import GenCol.entity;

public class DW_1_1 extends ViewableAtomic{

	private static final message NULL_MESSAGE = new message();
	//in port
	private static final String LOAD = "load";
	private static final String STOP = "stop";
	//out port
	private static final String STATS = "stats";
	private static final String HALT = "halt";
	//phases
    private static final String PASSIVE = "Passive";
	private static final String BUSY = "Busy";
	private static final String HALTING = "Halting";
	private static final String QUEUEING = "QueueingCat";
	private static final String EXT_CAT_IN = "ExtCatIn";
	private static final String LOADING = "Loading";
	/**
	 * Takes 1 unit of time to queue an incoming Cat File 
	 */
	private double QUEUEING_TIME = 1;
    
    
	private int numberOfProcessors;
	private message haltMessage;
	private Queue extCatFileQueue;
	private double processingTime;
	private Queue processedExtCatFileQueue;
	private message outputMessage;

    // Add Default Constructor
    public DW_1_1(){
        this("DW", 2);
    }

    // Add Parameterized Constructors
    public DW_1_1(String name, int numberOfProcessors){
        super(name);
        this.numberOfProcessors = numberOfProcessors;
// Structure information start
        // Add input port names
        addInport(EXT_CAT_IN);
        addInport(LOAD);
        addInport(STOP);

        // Add output port names
        addOutport(STATS);
        addOutport(HALT);

//add test input ports:
        addTestInput(EXT_CAT_IN, new ExtCatFile("ExtCat1", 100, "1", 2011, 10));
        addTestInput(EXT_CAT_IN, new ExtCatFile("ExtCat2", 100, "1", 2011, 10));
        addTestInput(EXT_CAT_IN, new ExtCatFile("ExtCat3", 100, "1", 2011, 10));
        addTestInput(EXT_CAT_IN, new ExtCatFile("ExtCat4", 80, "2", 2011, 8));
        addTestInput(EXT_CAT_IN, new ExtCatFile("ExtCat5", 20, "3", 2011, 2));

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = PASSIVE;
        sigma = INFINITY;
        extCatFileQueue = new Queue();
        processedExtCatFileQueue = new Queue();
    }

    // Add external transition function
    public void deltext(double e, message x){
    	for (int i = 0 ; i < x.getLength(); i++) {
    		checkForStop(x, i);
    	}
    	for (int i = 0 ; i < x.getLength(); i++) {
    		checkForLoad(x, i);
    	}
    	if (phaseIs(BUSY)) {
    		queueExtCatFiles(x, e);
    	} 
    	if (phaseIs(PASSIVE)) {
    		checkForExtCat(x);
    	}
    }

    /**
     * Checks if the loading of ExtCat should start
     * 
     * @param x
     * @param i
     */
    private void checkForLoad(message x, int i) {
    	if (messageOnPort(x, LOAD, i)) {
    		entity value = x.getValOnPort(LOAD, i);
    		if (value.getName().equals(LOAD)) {
    			processingTime = processFiles();
    			holdIn(LOADING, processingTime);
    		}
    	}
	}

	/**
     * Queues any <code>ExtCatFile</code> that comes
     * in while the <tt>DW</tt> is <code>BUSY</code>
     * @param x
     * @param e 
     */
    @SuppressWarnings("unchecked")
	private void queueExtCatFiles(message x, double e) {
    	for (int i = 0; i < x.getLength(); i++) {
    		if (messageOnPort(x, LOAD, i)) {
    			entity value = x.getValOnPort(LOAD, i);
    			if (value instanceof ExtCatFile) {
    				Continue(e);
    				holdIn(QUEUEING, QUEUEING_TIME);
    				ExtCatFile aCatFile = (ExtCatFile) value;
    				extCatFileQueue.add(aCatFile);
    			}
    		}
    	}
	}

	/**
     * Picks up all <code>ExtCatFile</code> that are on
     * input port <code>LOAD</code>
     * 
     * @param x
     */
    @SuppressWarnings("unchecked")
	private void checkForExtCat(message x) {
    	for (int i = 0; i < x.getLength(); i++) {
    		if (messageOnPort(x, LOAD, i)) {
    			entity value = x.getValOnPort(LOAD, i);
    			if (value instanceof ExtCatFile) {
    				ExtCatFile aCatFile = (ExtCatFile) value;
    				extCatFileQueue.add(aCatFile);
    			}
    		}
    	}
    	//TODO: queue these separately
//		if (!extCatFileQueue.isEmpty()) {
//			processingTime = processFiles();
//			holdIn(BUSY, processingTime);
//			prepareOutput();
//		}
	}

    /**
     * prepares output message
     */
    private void prepareOutput() {
    	outputMessage = new message();
    	int size = processedExtCatFileQueue.size();
    	for (int i = 0; i < size; i++) {
    		ExtCatFile aFile = (ExtCatFile)processedExtCatFileQueue.remove();
        	outputMessage.add(makeContent(STATS, aFile));
    	}
	}

	/**
     * Assign the file to a process and wait
     * 
     * @return
     */
	@SuppressWarnings("unchecked")
	private double processFiles() {
		double processingTime = 0D;
		for (int i = 0; i < numberOfProcessors; i++) {
			ExtCatFile aFile = (ExtCatFile) extCatFileQueue.remove();
			if (aFile != null) {
				processingTime += aFile.getProcessingTime();
				processedExtCatFileQueue.add(aFile);
			}
		}
		return processingTime;
	}

	/**
     * Checks if there is a halt command from the Experimental Frame
     * @param x
     * @param i
     */
    private void checkForStop(message x, int i) {
    	if (messageOnPort(x, STOP, i)) {
    		entity value = x.getValOnPort(STOP, i);
    		if (value.getName().equals(STOP)) {
    			holdIn(HALTING, 1);
    			haltMessage = new message();
    			haltMessage.add(makeContent(HALT, new entity(HALT)));
    		}
    	}
	}

	// Add internal transition function
    public void deltint(){
    	if (phaseIs(HALTING)) {
    		passivateIn(PASSIVE);
    	} else if (phaseIs(BUSY)) {
			if (extCatFileQueue.isEmpty()) {
				passivateIn(PASSIVE);
			} else {
				holdIn(BUSY, processFiles());
				prepareOutput();
			}
    	} else {
    		if (phaseIs(QUEUEING)) {
    			holdIn(BUSY, processingTime);
    		}
    	}  
    }

    // Add confluent function
    public void deltcon(double e, message x){
    	deltint();
    	deltext(0, x);
    }

    // Add output function
    public message out(){
    	if (phaseIs(BUSY)) {
    		return outputMessage;
    	}
    	return NULL_MESSAGE;
    }

	@Override
	public void showState() {
		super.showState();
		System.out.println("The ExtCatFile Queue has " + extCatFileQueue.size() + " elements");
		System.out.println("The Queue contains: " + extCatFileQueue);
		System.out.println("The ProcessedExtCatFile Queue has " + processedExtCatFileQueue.size() + " elements");
		System.out.println("The Queue contains: " + processedExtCatFileQueue);
	}
	
	@Override
	public String getTooltipText() {
		StringBuilder myBuilder = new StringBuilder();
		myBuilder.append("\n");
		myBuilder.append("Ext Cat File Queue: ");
		myBuilder.append(extCatFileQueue);
		return super.getTooltipText() + myBuilder.toString();
	}
}
