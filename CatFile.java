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
	private double originalTimeToRegister;
	private double timeToRegister;
	private int numberOfDimensions;
	private int numberOfSummaryLevels;
	private int years;
	private double arrivalTime;
	private double completionTime;
	
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
	 * @param timeToRegister
	 * @param dimensions
	 * @param summaryLevel
	 * @param years
	 */
	public CatFile(String name, int numberOfRecords, double timeToRegister, int dimensions, int summaryLevels, int years) {
		super(name);
		this.numberOfRecords = numberOfRecords;
		this.timeToRegister = timeToRegister;
		this.originalTimeToRegister = timeToRegister;
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
		this.timeToRegister -= e;
	}
	public double getTimeToRegister() {
		return timeToRegister;
	}
	public double getOriginalTimeToRegister() {
		return originalTimeToRegister;
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
	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(double completionTime) {
		this.completionTime = completionTime;
	}
}
