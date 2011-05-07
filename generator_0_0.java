/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package DWLProject;


import java.awt.Color;

import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;
import DWLProject.utils.DWLProperties;
import GenCol.entity;

/**
 * 
 * @author visoso
 *
 */
public class generator_0_0 extends ViewableAtomic {

	private static final double FF_REGISTRATION_TIME = 1D;
	protected double int_arr_time;
	protected int count;
	protected int records = Integer.valueOf(DWLProperties.getInstance().getValue("NumberOfRecords"));
	protected int errors = Integer.valueOf(DWLProperties.getInstance().getValue("NumberOfErrors"));
	protected int categories = Integer.valueOf(DWLProperties.getInstance().getValue("NumberOfCategories"));
	protected int numberOfYears = Integer.valueOf(DWLProperties.getInstance().getValue("NumberOfYears"));

	// Phases
	private static final String READY = "ready";
	private static final String START_DW = "startingDW";
	private static final String SEND_FF = "sendingFF";

	public generator_0_0() {
		this("generator", 1);
	}

	public generator_0_0(String name, double Int_arr_time) {
		super(name);
		addOutport("start");
		addOutport("FFout");
		int_arr_time = Int_arr_time;
		initialize();
	}

	public void initialize() {
		holdIn(READY, 1);
		this.setBackgroundColor(Color.MAGENTA);

		count = 1000;
		super.initialize();
	}

	public void deltext(double e, message x) {
		Continue(e);
	}

	public void deltint() {
		if (phaseIs(READY)) {
			holdIn(START_DW, 1);
			this.setBackgroundColor(Color.MAGENTA);
		} else if (phaseIs(START_DW)) {
			holdIn(SEND_FF, 0);
			this.setBackgroundColor(Color.MAGENTA);
		} else if (phaseIs(SEND_FF)) {
			passivate();
			this.setBackgroundColor(Color.GRAY);
		}
	}

	public message out() {
		message m = new message();
		if (phaseIs(READY)) {
			content con = makeContent("start", new entity("start"));
			m.add(con);
		} else if (phaseIs(SEND_FF)) {
			FlatFile ff = new FlatFile(records, errors, categories, numberOfYears, FF_REGISTRATION_TIME);
			m.add(makeContent("FFout", ff));
		}
		return m;
	}

	public void showState() {
		super.showState();
		System.out.println("int_arr_t: " + int_arr_time);
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + " #Records: " + records + "\n"
			+ " #Errors: " + errors + "\n" + "  #Categories: " + categories + "\n";
	}

}
