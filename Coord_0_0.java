/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/20/11 1:09 PM
*/

// Default Package
package GeneratedModelsDEVS_Suite;

import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.entity;

/**
 * Coord_0_0.java
 * 
 * This class models the Coordinator in the Data Warehouse
 * loader system.
 * 
 * @author CosMos
 *
 */
public class Coord_0_0 extends ViewableAtomic{

    private static final String GET_FF = "GetFF";
	private static final String FF_OUT = "FFout";
	private static final String CAT_OUT = "CatOut";
	private static final String CAT_FILE_IN = "CatFileIn";
	private static final String LDR_DONE = "LdrDone";
	private static final String FF_IN = "FFin";
	private static final String START = "start";
	private static final String COORD_0_0 = "Coord_0_0";
	private static final String PASSIVE = "passive";
	private static final String DP_DONE = "DPDone";
	private static final String RECEIVE_FF = "ReceiveFF";
	protected double processing_time;

    // Add Default Constructor
    public Coord_0_0(){
        this(COORD_0_0);    }

    // Add Parameterized Constructors
    public Coord_0_0(String name){
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

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = PASSIVE;
        sigma = INFINITY;
    }

    // Add external transition function
    public void deltext(double e, message x){
		Continue(e);
		for (int i = 0; i < x.size(); i++) {
			checkForStartInput(x, i);
		}
    	
    	//TODO: passive -> start on start port -> ReceiveFF
    	//TODO: passive -> FF on FFin port -> SendFF
    	//TODO: passive -> Cat file on CAT_FILE_IN port -> ReceiveCat
    	//TODO: passive -> done on port DPDone -> SendCAT
    }

    private void checkForStartInput(message x, int i) {
		if (messageOnPort(x, START, i)) {
			entity value = x.getValOnPort(START, i);
			if (value.getName().equals(START)) {
				holdIn(RECEIVE_FF, INFINITY);
			}
		}
	}

	// Add internal transition function
    public void deltint(){
    	//TODO: ReceiveFF -> passive;
    	//TODO: SendFF -> passive;
    	//TODO: ReceiveCat -> passive;
    	//TODO: SendCAT -> if no more Cat files -> passive;
    }

    /**
     * Confluent function
     */
    public void deltcon(double e, message x){
    	deltint();
    	deltext(0D, x);
    }

    // Add output function
    public message out(){
    	return null;
    }

    // Add Show State function
    }

