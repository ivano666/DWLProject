/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package DWLProject;


import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;
import GenCol.entity;


/**
 * 
 * @author visoso
 *
 */
public class generator_0_0 extends ViewableAtomic {

	protected double int_arr_time;
	protected int count;
	protected int records = 1000;
	protected int errors = 0;
	protected int categories = 5;
	protected int numberOfYears = 1;

	// Phases
	private static final String PASSIVE = "passive";
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

		count = 1000;
		super.initialize();
	}

	public void deltext(double e, message x) {
		Continue(e);
	}

	public void deltint() {
		if (phaseIs(READY)) {
			holdIn(START_DW, 1);
		} else if (phaseIs(START_DW)) {
			holdIn(SEND_FF, 0);
		} else if (phaseIs(SEND_FF)) {

			passivate();
		}
	}

	public message out() {
		message m = new message();
		if (phaseIs(READY)) {
			content con = makeContent("start", new entity("start"));
			m.add(con);
		} else if (phaseIs(SEND_FF)) {
			FlatFile ff = new FlatFile();
			ff.setNumberOfRecords(records);
			ff.setNumberOfErrors(errors);
			ff.setNumberOfCategories(categories);
			ff.setNumberOfYears(numberOfYears);
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
				+ " #Categories: " + categories + "\n" + " #Errors: " + errors;
	}

}
