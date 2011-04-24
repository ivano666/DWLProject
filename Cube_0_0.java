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

public class Cube_0_0 extends ViewableAtomic{

    protected double processing_time;

    // Add Default Constructor
    public Cube_0_0(){
        this("Cube_0_0");    }

    // Add Parameterized Constructors
    public Cube_0_0(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("In Port");

        // Add output port names
        addOutport("Out Port");

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = "passive";
        sigma = INFINITY;
    }

    // Add external transition function
    public void deltext(double e, message x){
    }

    // Add internal transition function
    public void deltint(){
    }

    // Add confluent function
    public void deltcon(double e, message x){
    }

    // Add output function
    public message out(){
    	return null;
    }

    // Add Show State function
    }

