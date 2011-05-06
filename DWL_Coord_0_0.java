/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/20/11 1:09 PM
*/

// Default Package
package DWLProject;

import java.awt.Color;
import java.util.List;

import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.Pair;
import GenCol.Queue;
import GenCol.entity;

/**
 * Coord_0_0.java
 * 
 * This class models the Coordinator in the Data Warehouse
 * loader system.
 * 
 * @author CosMos, Ivan C, Enrique V
 *
 */
public class DWL_Coord_0_0 extends ViewableAtomic{
	
	private static final message NULL_MESSAGE = new message();
	private static final String COORD = "Coordinator";
	//Input Ports
	private static final String START = "start";
	private static final String FF_IN = "FFin";
	private static final String CAT_FILE_IN = "CatFileIn";
	private static final String LDR_DONE = "LdrDone";
	private static final String DP_DONE = "DPDone";

	//Output Ports
    private static final String GET_FF = "GetFF";
	private static final String FF_OUT = "FFout";
	private static final String CAT_OUT = "CatOut";
	private static final String LOAD = "load";
	
	
	//Phases
	private static final String PASSIVE = "passive";
	private static final String NOTIFY_CA = "NotifyCA";
	private static final String RECEIVE_FF = "ReceivingFF";
	private static final String SEND_FF = "SendingFF";
	private static final String SEND_CAT = "SendingCats";
	private static final String RECEIVE_CAT = "ReceivingCat";
	private static final String QUEUEING = "QueueingCat";
	private static final String LDRS_DONE = "LoadersDone";
	/**
	 * Takes 1 unit of time to queue an incoming Cat File 
	 */
	private double QUEUEING_TIME = 1;
	
	private CatFile currentCatFile;
	private Queue catFileQueue;
	private Queue workingCatFileQueue;
	private Queue pendingCatFileQueue;
	private Queue completedCatQueue;
	private Queue loadersQueue;
	private message loadFileMessage;
	private message partitionFileMessage;
	private message catFilesOutMessage;
	private boolean doneDPReceived;
	private message loadersDoneMessage;

	/**
	 * Default Constructor
	 */
    public DWL_Coord_0_0(){
        this(COORD);    }

    /**
     * Parameterized Constructor
     * 
     * @param name
     */
    public DWL_Coord_0_0(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport(START);
        addInport(FF_IN);
        addInport(DP_DONE);
        addInport(LDR_DONE);
        addInport(CAT_FILE_IN);

        // Add output port names
        addOutport(CAT_OUT);
        addOutport(FF_OUT);
        addOutport(GET_FF);
        addOutport(LOAD);

//add test input ports:
        addTestInput(START, new entity(START));
        addTestInput(FF_IN, new FlatFile());
        addTestInput(CAT_FILE_IN, new CatFile("Cat1", 10, 10, 3, 1, 2011));
        addTestInput(CAT_FILE_IN, new CatFile("Cat2", 10, 20, 2, 1, 2011), 5);
        addTestInput(CAT_FILE_IN, new CatFile("Cat3", 10, 10, 5, 1, 2011), 15);
        addTestInput(DP_DONE, new entity(DP_DONE));

// Structure information end
        loadersQueue = new Queue();
        initialize();
    }

	@Override
    public void initialize(){
        super.initialize();
        phase = PASSIVE;
        sigma = INFINITY;
        catFileQueue = new Queue();
        workingCatFileQueue = new Queue();
        completedCatQueue = new Queue();
        pendingCatFileQueue = new Queue();
        currentCatFile = null;
        loadFileMessage = null;
        partitionFileMessage = null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void deltext(double e, message x){
		Continue(e);
		if (phaseIs(RECEIVE_CAT)) {
			for (int i = 0; i < x.size(); i++) {
				queueCatFile(x, i, e);
			}
		}
		checkForStartInput(x);
		checkForFlatFile(x);
		checkForCatFile(x);
		checkForDPDone(x);
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, LDR_DONE, i)) {
				entity value = x.getValOnPort(LDR_DONE, i);
				Pair pair = (Pair) value;
				loadersQueue.add(pair.getKey());
				CatFile aCatFile = (CatFile) pair.getValue();
				completedCatQueue.add(aCatFile);
			}
		}
    	if (phaseIs(PASSIVE) && doneDPReceived) {
    		holdIn(SEND_CAT, 1);
    		sendCatFilesToLoaders();
    	}
    }

    /**
     * Checks if the <code>DataPartitioner</code> has notified is done
     * sending files and thus 
     * @param x
     */
    private void checkForDPDone(message x) {
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, DP_DONE, i)) {
				entity value = x.getValOnPort(DP_DONE, i);
				if (value.getName().equals(DP_DONE)) {
					doneDPReceived = true;
				}
			}
		}
	}

    /**
     * Creates the messages for the loaders 
     */
	private void sendCatFilesToLoaders() {
		catFilesOutMessage = new message();
		while (!loadersQueue.isEmpty() && !workingCatFileQueue.isEmpty()) {
			Loader_0_0 aLoader = (Loader_0_0)loadersQueue.remove();
			CatFile aCatFile = (CatFile) workingCatFileQueue.remove();
			Pair aPair = new Pair(aLoader.getName(), aCatFile);
			catFilesOutMessage.add(makeContent(CAT_OUT, aPair));
		}
	}

	/**
     * It puts the <code>CatFile</code> in a pending queue, updates sigma
     * and continues receiving the current <code>CatFile</code>
     * 
     * @param x message
     * @param i 
     * @param e
     */
    @SuppressWarnings("unchecked")
	private void queueCatFile(message x, int i, double e) {
		if (messageOnPort(x, CAT_FILE_IN, i)) {
			entity value = x.getValOnPort(CAT_FILE_IN, i);
			if (value instanceof CatFile) {
				CatFile aCatFile = (CatFile) value;
				currentCatFile.updateRegistrationTime(e);
				holdIn(QUEUEING, QUEUEING_TIME);
				pendingCatFileQueue.add(aCatFile);
				this.setBackgroundColor(Color.GREEN);
			}
			else {
				System.out.println("Not a Cat File: " + value.getName());
				holdIn(PASSIVE, INFINITY);
				this.setBackgroundColor(Color.GRAY);
			}
		}
	}

	/**
     * Checks if a <code>CatFile</code> is in port <code>CAT_FILE_IN</code>
     * and stores it in an internal queue that is used later on to provide work to
     * the different <tt>loaders</tt>
     * 
     * @param x
     */
    @SuppressWarnings("unchecked")
	private void checkForCatFile(message x) {
    	if (phaseIs(PASSIVE)) {
			for (int i = 0; i < x.size(); i++) {
	    		if (messageOnPort(x, CAT_FILE_IN, i)) {
	    			entity value = x.getValOnPort(CAT_FILE_IN, i);
	    			if (value instanceof CatFile) {
	    				CatFile aCatFile = (CatFile) value;
	    				holdIn(RECEIVE_CAT, aCatFile.getTimeToRegister());
	    				currentCatFile = aCatFile;
	    				workingCatFileQueue.add(currentCatFile);
	    				catFileQueue.add(currentCatFile);
	    				this.setBackgroundColor(Color.GREEN);
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

	/**
     * Checks if the <code>FlatFile</code> is in the input port
     * <code>FF_IN</code> and prepares to send it to the
     * <code>FF_OUT</code> port
     * 
     * @param x
     */
    private void checkForFlatFile(message x) {
		if (phaseIs(PASSIVE)) {
			for (int i = 0; i < x.size(); i++) {
				if (messageOnPort(x, FF_IN, i)) {
					entity value = x.getValOnPort(FF_IN, i);
					if (value instanceof FlatFile) {
						FlatFile theFlatFile = (FlatFile) value;
						partitionFileMessage = new message();
						partitionFileMessage.add(makeContent(FF_OUT, theFlatFile));
						holdIn(RECEIVE_FF, theFlatFile.getTimeToRegister());
						this.setBackgroundColor(Color.GREEN);
					}
					else {
						System.out.println("Not a Flat File: " + value.getName());
						holdIn(PASSIVE, INFINITY);
						this.setBackgroundColor(Color.GRAY);
					}
				}
			}
		}
	}

	/**
     * Checks for <code>START</code> on the input port <tt>start</tt>
     * and prepares to send a message to the <code>CommAgent</code>
     * thru the <code>GET_FF</code> port
     * 
     * @param x
     */
	private void checkForStartInput(message x) {
		if (phaseIs(PASSIVE)) {
			for (int i = 0; i < x.size(); i++) {
				if (messageOnPort(x, START, i)) {
					entity value = x.getValOnPort(START, i);
					if (value.getName().equals(START)) {
						holdIn(NOTIFY_CA, 0);
						loadFileMessage = new message();
						loadFileMessage.add(makeContent(GET_FF, new entity(START)));
						this.setBackgroundColor(Color.GREEN);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
    public void deltint(){
    	if (phaseIs(NOTIFY_CA)) {
    		passivateIn(PASSIVE);
			this.setBackgroundColor(Color.GRAY);
    	}
    	if (phaseIs(SEND_FF)) {
    		passivateIn(PASSIVE);
			this.setBackgroundColor(Color.GRAY);
    	}
    	if (phaseIs(RECEIVE_FF)) {
    		holdIn(SEND_FF, 0);
			this.setBackgroundColor(Color.GREEN);
    	}
    	if (phaseIs(QUEUEING)) {
    		double timeLeftForRegistration = currentCatFile.getTimeToRegister();
    		if (timeLeftForRegistration >= 0D) {
    			holdIn(RECEIVE_CAT, timeLeftForRegistration);
    			this.setBackgroundColor(Color.GREEN);
    		} else {
    			currentCatFile = null;
    			holdIn(PASSIVE, INFINITY);
    			this.setBackgroundColor(Color.GRAY);
    		}
    	}
    	if (phaseIs(RECEIVE_CAT)) {
			if (pendingCatFileQueue.size() > 0) {
				currentCatFile = (CatFile) pendingCatFileQueue.remove();
				workingCatFileQueue.add(currentCatFile);
				catFileQueue.add(currentCatFile);
				holdIn(RECEIVE_CAT, currentCatFile.getTimeToRegister());
				this.setBackgroundColor(Color.GREEN);
			} else {
				passivateIn(PASSIVE);
				currentCatFile = null;
				this.setBackgroundColor(Color.GRAY);
			}
    	}
    	if (phaseIs(LDRS_DONE)) {
    		passivateIn(PASSIVE);
			this.setBackgroundColor(Color.GRAY);
    	}
    	if (phaseIs(SEND_CAT)) {
    		if (catFileQueue.size() == completedCatQueue.size()) {
    			holdIn(LDRS_DONE, 0);
        		loadersDoneMessage = new message();
        		loadersDoneMessage.add(makeContent(LOAD, new entity("start")));
    			this.setBackgroundColor(Color.GREEN);
    		} else {
    			sendCatFilesToLoaders();
    		}
    	}
    }

    @Override
    public void deltcon(double e, message x){
    	deltint();
    	deltext(0D, x);
    }

//    @SuppressWarnings("unchecked")
	@Override
    public message out(){
    	if (phaseIs(NOTIFY_CA)) {
// TODO: Coupled model inside a coupled model does not refresh appropriately
//			loadersQueue.addAll(LoaderManager.addLoadersToSystem(2, this));
    		return loadFileMessage;
    	}
    	if (phaseIs(SEND_FF)) {
    		return partitionFileMessage;
    	}
    	if (phaseIs(SEND_CAT)) {
    		return catFilesOutMessage;
    	}
    	if (phaseIs(LDRS_DONE)) {
    		return loadersDoneMessage;
    	}
    	return NULL_MESSAGE;
    }

    /**
     * @return <code>CAT_OUT</code> port
     */
	public static String getCatOut() {
		return CAT_OUT;
	}

	public static String getLdrDone() {
		return LDR_DONE;
	}

	@Override
	public void showState() {
		super.showState();
		System.out.println("The CatFile Queue has " + workingCatFileQueue.size() + " elements");
		System.out.println("The Queue contains: " + workingCatFileQueue);
		System.out.println("The Completed Loading Cat File Queue has " + completedCatQueue.size() + " elements");
		System.out.println("The Queue contains: " + completedCatQueue);
		System.out.println("The Loaders Queue has " + loadersQueue.size() + " elements");
		System.out.println("The Queue contains: " + loadersQueue);
	}
	
	@Override
	public String getTooltipText() {
		StringBuilder myBuilder = new StringBuilder();
		myBuilder.append("\n");
		myBuilder.append("Loaders Queue: ");
		myBuilder.append(loadersQueue);
		myBuilder.append("\n");
		myBuilder.append("Cat File Queue: ");
		myBuilder.append(workingCatFileQueue);
		return super.getTooltipText() + myBuilder.toString();
	}
	
	/**
	 * Set the loaders into the queue
	 * @param loaders
	 */
	@SuppressWarnings("unchecked")
	public void setLoaders(List<Loader_0_0> loaders) {
		loadersQueue.addAll(loaders);
	}
}

