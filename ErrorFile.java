package DWLProject;

import GenCol.entity;

/**
* ErrorFile.java
* <p>
* Holds the records in error found in Flat File by the Data Partitioner
* <p>
* This file is loaded by the <code>DP</code>
*
* @author herrera (cloned from icaspeta)
*
*/
public class ErrorFile extends entity {
private static final String ERROR_FILE_TXT = "ErrorFile";
private int numberOfRecords;
private double registrationTime;

public ErrorFile() {
super(ERROR_FILE_TXT);
this.numberOfRecords = 100;
this.registrationTime = 10D;
}

/**
* Constructor using fields
* @param numberOfRecords
* @param numberOfErrors
* @param numberOfCategories
* @param registrationTime
*/
public ErrorFile(int numberOfRecords, double registrationTime) {
super(ERROR_FILE_TXT);
this.numberOfRecords = numberOfRecords;
this.registrationTime = registrationTime;
}
public int getNumberOfRecords() {
return numberOfRecords;
}
public void setNumberOfRecords(int numberOfRecords) {
this.numberOfRecords = numberOfRecords;
}
/*
public int getNumberOfCategories() {
return numberOfCategories;
}
public void setNumberOfCategories(int numberOfCategories) {
this.numberOfCategories = numberOfCategories;
} */
public double getRegistrationTime() {
return this.registrationTime;
}
}

