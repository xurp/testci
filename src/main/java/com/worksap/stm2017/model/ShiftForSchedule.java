package com.worksap.stm2017.model;

public class ShiftForSchedule {
private String name;
private double hour;
public ShiftForSchedule(){
	
}
public ShiftForSchedule(String name, double hour){
	this.name=name;
	this.hour=hour;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public double getHour() {
	return hour;
}
public void setHour(double hour) {
	this.hour = hour;
}

}
