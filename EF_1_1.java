/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 4/20/11 1:09 PM
*/

// Default Package
package GeneratedModelsDEVS_Suite;

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

        // Add output port names
        addOutport("start");
        addOutport("FFout");

//add test input ports:

        // Initialize sub-components
        ViewableAtomic OperationalSystem_1_0 =  new OperationalSystem_0_0("OperationalSystem_1_0");

        // Add sub-components
        add(OperationalSystem_1_0);

        // Add Couplings
        addCoupling(this, "FFout", OperationalSystem_1_0, "FFout");
        addCoupling(this, "start", OperationalSystem_1_0, "start");

// Structure information end
        initialize();
        }

    }
