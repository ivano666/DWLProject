/**
 * 
 */
package DWLProject;

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

	private static final double DEFAULT_REGISTRATION_TIME = 10D;
	private static final int DEFAULT_NUMBER_OF_RECORDS = 100;
	private static final String CAT1 = "Cat1";
	private int numberOfRecords;
	private double completeRegistrationTime;
	private double registrationTime;
	
	/**
	 * Default Constructor
	 */
	public CatFile() {
		this(CAT1, DEFAULT_NUMBER_OF_RECORDS, DEFAULT_REGISTRATION_TIME);
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param name
	 * @param registrationTime
	 */
	public CatFile(String name, int numberOfRecords, double registrationTime) {
		super(name);
		this.numberOfRecords = numberOfRecords;
		this.registrationTime = registrationTime;
		this.completeRegistrationTime = registrationTime;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}
	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	public void updateRegistrationTime(double e) {
		this.registrationTime -= e;
	}
	public double getRegistrationTime() {
		return registrationTime;
	}
	public double getCompleteRegistrationTime() {
		return completeRegistrationTime;
	}
}
