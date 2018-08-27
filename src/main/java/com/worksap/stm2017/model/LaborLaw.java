package com.worksap.stm2017.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity 
public class LaborLaw {
private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id; 
	
	@Column(nullable = false, length = 50) 
	private double hour;
	
	@Column(nullable = false, length = 50) 
	private String use;
	
	protected LaborLaw(){}
	
	LaborLaw(double hour, String use){
		this.hour=hour;
		this.use=use;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getHour() {
		return hour;
	}

	public void setHour(double hour) {
		this.hour = hour;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}
	
}
