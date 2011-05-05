/**
 * 
 */
package DWLProject;

import DWLProject.utils.ExtCatFileStatus;
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
	private double processingTime;
	private double arrivalTime;
	private double completionTime;
	private double timeToRegister;
	private CatFile parentCatFile;
	private ExtCatFileStatus status = ExtCatFileStatus.NONE;
	
	public ExtCatFile() {
		super(EXT_CAT1);
	}

	/**
	 * Parameterized constructor
	 * @param name
	 * @param numberOfRecords
	 * @param summaryLevel
	 * @param year
	 * @param processingTime
	 * @param timeToRegister
	 */
	public ExtCatFile(String name, int numberOfRecords, String summaryLevel, int year, double processingTime, double timeToRegister) {
		super(name);
		this.numberOfRecords = numberOfRecords;
		this.summaryLevel = summaryLevel;
		this.year = year;
		this.processingTime = processingTime;
		this.timeToRegister = timeToRegister;
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

	public double getProcessingTime() {
		return processingTime;
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

	public ExtCatFileStatus getStatus() {
		return status;
	}

	public void setStatus(ExtCatFileStatus status) {
		this.status = status;
	}

	public double getTimeToRegister() {
		return timeToRegister;
	}
	public void updateTimeToRegister(double e) {
		timeToRegister -=e;
	}

	public void setTimeToRegister(double regTime) {
		timeToRegister = regTime;
	}

	public CatFile getParentCatFile() {
		return parentCatFile;
	}

	public void setParentCatFile(CatFile parentCatFile) {
		this.parentCatFile = parentCatFile;
	}
	
}
