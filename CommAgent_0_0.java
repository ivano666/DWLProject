/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/20/11 1:09 PM
*/

// Default Package
package DWLProject;

import java.awt.Color;

import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.entity;

public class CommAgent_0_0 extends ViewableAtomic{

    protected double processing_time;
    
    private message loadFileMessage;
    
  //Phases
	private static final String PASSIVE = "passive";
	private static final String RECEIVE_FF = "Receive";
	private static final String SEND_FF = "Send";
	
	//Input Ports
	private static final String FF_IN = "FFin";
	//Output Ports
	private static final String FF_OUT = "FFout";
	
	private static final String START = "start";
	
	//private static final String DONE = "Done";
    // Add Default Constructor
    public CommAgent_0_0(){
        this("CommAgent_0_0");    }

    // Add Parameterized Constructors
    public CommAgent_0_0(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("FFin");
        addInport("start");

        // Add output port names
        addOutport("FFout");

//add test input ports:
        addTestInput(FF_IN, new FlatFile());

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
    	if (phaseIs(PASSIVE)) {
			for (int i = 0; i < x.size(); i++) {
				checkForStartInput(x, i);
			}
    	} else{
        	if (phaseIs(RECEIVE_FF)) {
    			for (int i = 0; i < x.size(); i++) {
    				checkForFlatFile(x, i);
    			}
        	}
    	}
    }

	/**
     * Checks for <code>START</code> on the input port <tt>start</tt>
     * and prepares to send a message to the <code>CommAgent</code>
     * thru the <code>GET_FF</code> port
     * @param x
     * @param i
     */
    private void checkForStartInput(message x, int i) {
		
		if (messageOnPort(x, START, i)) {
			entity value = x.getValOnPort(START, i);
			if (value.getName().equals(START)) {
				holdIn(RECEIVE_FF, 5);
				this.setBackgroundColor(Color.CYAN);
			}
		}
	}
	/**
     * Checks if the <code>FlatFile</code> is in the input port
     * <code>FF_IN</code> and prepares to send it to the
     * <code>FF_OUT</code> port
     * @param x
     * @param i
     */
    private void checkForFlatFile(message x, int i) {
    	loadFileMessage = new message();
		if (messageOnPort(x, FF_IN, i)) {
			entity value = x.getValOnPort(FF_IN, i);
			if (value instanceof FlatFile) {
				FlatFile theFlatFile = (FlatFile) value;
				loadFileMessage.add(makeContent(FF_OUT, theFlatFile));
				holdIn(SEND_FF, 1);
			}
		}
	}
    
    // Add internal transition function
    public void deltint(){
    	if (phaseIs(SEND_FF)){
        		passivate();
        		this.setBackgroundColor(Color.GRAY);
    	};
    }

    // Add confluent function
    public void deltcon(double e, message x){
    }

    // Add output function
    public message out(){
    	if(phaseIs(SEND_FF)){
    		return loadFileMessage;
    	}else
    	return new message();
    }

    // Add Show State function
    }

