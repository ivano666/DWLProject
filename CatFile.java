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
	private int numberOfDimensions;
	private int numberOfSummaryLevels;
	private int years;
	
	/**
	 * Default Constructor
	 */
	public CatFile() {
		this(CAT1, DEFAULT_NUMBER_OF_RECORDS, DEFAULT_REGISTRATION_TIME, 3, 1, 1);
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param name
	 * @param numberOfRecords
	 * @param registrationTime
	 * @param dimensions
	 * @param summaryLevel
	 * @param years
	 */
<<<<<<< HEAD
	public CatFile(String name, int numberOfRecords, double completeRegistrationTime, int dimensions, int summaryLevels, int years) {
=======
	public CatFile(String name, int numberOfRecords, double registrationTime, int dimensions, int summaryLevels, int years) {
>>>>>>> upstream/master
		super(name);
		this.numberOfRecords = numberOfRecords;
		this.completeRegistrationTime = completeRegistrationTime;
		this.numberOfDimensions = dimensions;
		this.numberOfSummaryLevels = summaryLevels;
		this.years = years;
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
	public int getNumberOfDimensions() {
		return numberOfDimensions;
	}
	public int getNumberOfSummaryLevels() {
		return numberOfSummaryLevels;
	}
	public int getYears() {
		return years;
	}
}
