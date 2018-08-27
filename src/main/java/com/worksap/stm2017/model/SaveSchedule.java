package com.worksap.stm2017.model;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity 
public class SaveSchedule{
	
	
private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id; 
	
	@NotEmpty(message = "")
	@Size(min=1, max=50,message="size of file name should between 1 to 50")
	@Column(nullable = false, length = 50, unique = true) 
	private String name;

	@Column(nullable=false)
	@CreationTimestamp
	private Timestamp createTime;
	
	@Column(nullable = false, length = 65000)
	private String dates; 

	@Column(nullable = false, length = 65000)
	private String shifts;

	@Column(nullable = false, length = 65000)
	private String saves;

    @Column(nullable = false, length = 255)
	private String selectedshiftgroup;

	
	
	protected SaveSchedule() { 
	}
	public SaveSchedule(String name, String dates,String shifts,String saves,String selectedshiftgroup) {
		this.name = name;
		this.dates = dates;
		this.shifts = shifts;
		this.saves = saves;
		this.selectedshiftgroup=selectedshiftgroup;
	}

	public Timestamp getCreateTime(){
	    return createTime;
	}

    public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setSelectedshiftgroup(String selectedshiftgroup) {
		this.selectedshiftgroup = selectedshiftgroup;
	}
	public String getSelectedshiftgroup() {
		return selectedshiftgroup;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public String getShifts() {
		return shifts;
	}
	public void setShifts(String shifts) {
		this.shifts = shifts;
	}
	public String getSaves() {
		return saves;
	}
	public void setSaves(String saves) {
		this.saves = saves;
	}
	

	
	
}