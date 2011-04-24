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

public class Loader_0_0 extends ViewableAtomic{

    protected double processing_time;

    // Add Default Constructor
    public Loader_0_0(){
        this("Loader_0_0");    }

    // Add Parameterized Constructors
    public Loader_0_0(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("CatIn");

        // Add output port names
        addOutport("done");
        addOutport("insert");
        addOutport("update");

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

