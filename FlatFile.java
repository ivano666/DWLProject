package GeneratedModelsDEVS_Suite;

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
	
	public FlatFile() {
		super(FLAT_FILE_TXT);
	}
	
	/**
	 * Constructor using fields
	 * @param numberOfRecords
	 * @param numberOfErrors
	 * @param numberOfCategories
	 */
	public FlatFile(int numberOfRecords, int numberOfErrors,
			int numberOfCategories) {
		super(FLAT_FILE_TXT);
		this.numberOfRecords = numberOfRecords;
		this.numberOfErrors = numberOfErrors;
		this.numberOfCategories = numberOfCategories;
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
	
	

}
