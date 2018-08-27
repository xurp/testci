package com.worksap.stm2017.model;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 *  实体.
 */

public class SetDates{
	
	private static final long serialVersionUID = 1L;

	
	
	//@NotEmpty(message = "excludeStrings不能为空")
	//@Size(min=0, max=100)
	private String excludeDates;

	//@NotEmpty(message = "starttime不能为空")
	//@Size(max=50)
	//@StringTimeFormat(pattern = "yyyy-MM-dd")
	private String starttime;
	
	//@NotEmpty(message = "endtime不能为空")
	//@Size(max=50)
	//@StringTimeFormat(pattern = "yyyy-MM-dd")
	private String endtime;
	
	
	
	protected SetDates() { 
	}



	public SetDates(String excludeStrings,
			String starttime,
			String endtime) {
		super();
		this.excludeDates = excludeStrings;
		this.starttime = starttime;
		this.endtime = endtime;
	}



	public String getExcludeDates() {
		return excludeDates;
	}



	public void setExcludeDates(String excludeStrings) {
		this.excludeDates = excludeStrings;
	}



	public String getStarttime() {
		return starttime;
	}



	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}



	public String getEndtime() {
		return endtime;
	}



	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	
	
	
	
}