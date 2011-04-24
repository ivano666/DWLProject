/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/20/11 1:09 PM
*/

// Default Package
package DWLProject;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableDigraph;

public class EF_1_1 extends ViewableDigraph{

    // Add Default Constructor
    public EF_1_1(){
        this("EF_1_1");
    }

    // Add Parameterized Constructor
    public EF_1_1(String name){
        super(name);

// Structure information start
        // Add input port names
        addInport("stats");
        addInport("ErrorFile");

        // Add output port names
        addOutport("start");
        addOutport("FFout");
        addOutport("measures");

//add test input ports:

        // Initialize sub-components
        ViewableAtomic operationalSystem_1_0 =  new OperationalSystem_0_0("OperationalSystem_1_0");
        ViewableAtomic transducer_1_0 = new Transducer_0_0("Transducer_1_0");

        // Add sub-components
        add(operationalSystem_1_0);
        add(transducer_1_0);

        // Add Couplings
        addCoupling(this, "FFout", operationalSystem_1_0, "FFout");
        addCoupling(this, "start", operationalSystem_1_0, "start");
        addCoupling(this, "stats", transducer_1_0, "stats");
        addCoupling(this, "ErrorFile", transducer_1_0, "ErrorFile");
        addCoupling(this, "measures", transducer_1_0, "measures");

// Structure information end
        initialize();
        }

    }
