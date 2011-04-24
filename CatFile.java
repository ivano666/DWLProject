/**
 * 
 */
package GeneratedModelsDEVS_Suite;

import GenCol.entity;

/**
 * CatFile.java
 * <p>
 * This class models the <tt>category</tt> files
 * produced by the <code>DataPartitioner_0_0</code>
 * 
 * @author icaspeta
 *
 */
public class CatFile extends entity {

	private static final String CAT1 = "Cat1";
	private int numberOfRecords;
	
	/**
	 * Default Constructor
	 */
	public CatFile() {
		super(CAT1);
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param name
	 */
	public CatFile(String name, int numberOfRecords) {
		super(name);
		this.numberOfRecords = numberOfRecords;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	
}
