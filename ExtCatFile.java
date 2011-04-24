/**
 * 
 */
package GeneratedModelsDEVS_Suite;

import GenCol.entity;


/**
 * ExtCatFile.java
 * <p>
 * This class models the files produced by the different
 * loaders
 * 
 * @author icaspeta
 *
 */
public class ExtCatFile extends entity {
	private static final String EXT_CAT1 = "ExtCat1";
	private int numberOfRecords;
	private String summaryLevel;
	private int year;
	
	
	public ExtCatFile() {
		super(EXT_CAT1);
	}

	/**
	 * Parameterized constructor
	 * @param name
	 * @param numberOfRecords
	 * @param summaryLevel
	 * @param year
	 */
	public ExtCatFile(String name, int numberOfRecords, String summaryLevel, int year) {
		super(name);
		this.numberOfRecords = numberOfRecords;
		this.summaryLevel = summaryLevel;
		this.year = year;
	}


	public int getNumberOfRecords() {
		return numberOfRecords;
	}


	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}


	public String getSummaryLevel() {
		return summaryLevel;
	}


	public void setSummaryLevel(String summaryLevel) {
		this.summaryLevel = summaryLevel;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}
	
	
	
}
