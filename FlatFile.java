package DWLProject;

import GenCol.entity;

/**
 * FlatFile.java
 * <p>
 * Represents the Flat File with data that will be loaded into the
 * Data Warehouse.
 * <p>
 * This file is loaded by the <code>CommAgent</code>
 * 
 * @author icaspeta
 *
 */
public class FlatFile extends entity {
	private static final String FLAT_FILE_TXT = "FlatFile";
	private int numberOfRecords;
	private int numberOfErrors;
	private int numberOfCategories;
	private double registrationTime;
	private int numberOfYears;
	
	public FlatFile() {
		super(FLAT_FILE_TXT);
		this.numberOfRecords = 100;
		this.numberOfErrors = 0;
		this.numberOfCategories = 2;
		this.registrationTime = 10D;
		this.numberOfYears = 1;
	}
	
	/**
	 * Constructor using fields
	 * @param numberOfRecords
	 * @param numberOfErrors
	 * @param numberOfCategories
	 * @param numberOfYears
	 * @param registrationTime
	 */
	public FlatFile(int numberOfRecords, int numberOfErrors,
			int numberOfCategories, int numberOfYears, double registrationTime) {
		super(FLAT_FILE_TXT);
		this.numberOfRecords = numberOfRecords;
		this.numberOfErrors = numberOfErrors;
		this.numberOfCategories = numberOfCategories;
		this.registrationTime = registrationTime;
		this.numberOfYears = numberOfYears;
	}
	public int getNumberOfRecords() {
		return numberOfRecords;
	}
	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	public int getNumberOfErrors() {
		return numberOfErrors;
	}
	public void setNumberOfErrors(int numberOfErrors) {
		this.numberOfErrors = numberOfErrors;
	}
	public int getNumberOfCategories() {
		return numberOfCategories;
	}
	public void setNumberOfCategories(int numberOfCategories) {
		this.numberOfCategories = numberOfCategories;
	}
	public double getRegistrationTime() {
		return this.registrationTime;
	}
	public int getNumberOfYears() {
		return numberOfYears;
	}
	public void setNumberOfYears(int numberOfYears) {
		this.numberOfYears = numberOfYears;
	}
}
