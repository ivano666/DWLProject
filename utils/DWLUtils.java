package DWLProject.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.modeling.IODevs;
import view.modeling.ViewableAtomic;
import DWLProject.DWL_1_1;
import DWLProject.DWL_Coord_0_0;
import DWLProject.Loader_0_0;
import DWLProject.Writer_0_0;

/**
 * DWLUtils.java
 * 
 * This class manages creation of the loaders as well as 
 * adding them to the system
 * 
 * @author Ivan C
 *
 */
public class DWLUtils {
	
	private static final String LOAD = "load";
	private static final String LOADER = "Loader_";
	private static final String WRITER = "Writer_";

	private DWLUtils() {
	}

	/**
	 * Adds the <code>numberOfLoaders</code> of type <code>Loader_0_0</code> to the
	 * system using the <code>Coord_0_0</code> as the link up to the <tt>coupled
	 * parent model.</tt>
	 * 
	 * @param numberOfLoaders
	 * @param coord
	 * @return
	 */
	public static List<Loader_0_0> addLoadersToSystem(int numberOfLoaders, ViewableAtomic coord) {
		List<Loader_0_0> theLoaders = createLoaders(numberOfLoaders);
		for(Loader_0_0 loader : theLoaders) {
			addLoaderToSystem(loader, coord);
		} 
		return theLoaders;
	}
	
	/**
	 * Return a <code>List</code> of <code>Loader_0_0</code>
	 * 
	 * @param numberOfLoaders
	 * @return list of loaders
	 */
	public static List<Loader_0_0> createLoaders(int numberOfLoaders) {
		List<Loader_0_0> theList = new ArrayList<Loader_0_0>(numberOfLoaders);
		for (int i=0 ; i < numberOfLoaders; i++) {
			Loader_0_0 aLoader = new Loader_0_0(LOADER + (i+1));
			aLoader.setPreferredLocation(new Point(100, 80));
			theList.add(aLoader);
		}
		return theList;
	}

	/**
	 * Return a <code>List</code> of <code>Loader_0_0</code>
	 * 
	 * @param numberOfLoaders
	 * @return list of loaders
	 */
	public static List<Writer_0_0> createWriters(int numberOfLoaders) {
		List<Writer_0_0> theList = new ArrayList<Writer_0_0>(numberOfLoaders);
		for (int i=0 ; i < numberOfLoaders; i++) {
			Writer_0_0 aWriter = new Writer_0_0(WRITER + (i+1));
			aWriter.setPreferredLocation(new Point(200, 80));
			theList.add(aWriter);
		}
		return theList;
	}
	
	/**
	 * Adds the <code>Loader_0_0</code> to the system using the 
	 * <code>Coord_0_0</code> as link to the parent <tt>coupled model</tt>.
	 * It also creates the appropriate couplings
	 * 
	 * @param aLoader
	 * @param coordinator
	 */
	private static void addLoaderToSystem(Loader_0_0 aLoader, ViewableAtomic coordinator) {
		IODevs aDevs = coordinator.getParent();
		if (aDevs instanceof DWL_1_1) {
			DWL_1_1 theParent = (DWL_1_1) aDevs; 
			coordinator.addModel(aLoader);
			coordinator.addCoupling(coordinator.getName(), DWL_Coord_0_0.getCatOut(), aLoader.getName(), Loader_0_0.getCatIn());
			coordinator.addCoupling(aLoader.getName(), Loader_0_0.getDone(), coordinator.getName(), DWL_Coord_0_0.getLdrDone());
			coordinator.addCoupling(aLoader.getName(), Loader_0_0.getExtCatOut(), theParent.getName(), LOAD);
		}
	} 

}
