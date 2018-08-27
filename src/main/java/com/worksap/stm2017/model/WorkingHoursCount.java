package com.worksap.stm2017.model;

public class WorkingHoursCount {
private String name;
private double hours;
private int shift;
public WorkingHoursCount(){
	
}
public WorkingHoursCount(String name, double hours, int shift) {
	super();
	this.name = name;
	this.hours = hours;
	this.shift = shift;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public double getHours() {
	return hours;
}
public void setHours(double hours) {
	this.hours = hours;
}
public int getShift() {
	return shift;
}
public void setShift(int shift) {
	this.shift = shift;
}

}
