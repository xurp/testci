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
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * User 实体.
 */
@Entity // 实体
@Table(name="shift", schema = "xu_xi_rd2")
public class Shift{
	
	private static final long serialVersionUID = 1L;

	
	@Id // 主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 自增策略
	private Long id; // 实体一个唯一标识
	
	@NotEmpty(message = "shift name can not be empty")
	@Size(min=2, max=50,message="size of name should between 2 to 50")
	@Column(nullable = false, length = 50, unique = true) 
	private String name;

	@NotEmpty(message = "shift start time can not be empty")
	@Size(max=50)
	@Column(nullable = false, length = 50)
	private String starttime;
	
	@NotEmpty(message = "shift end time can not be empty")
	@Size(max=50)
	@Column(nullable = false, length = 50)
	private String endtime;
	
	//int类型好像不能用size/length/notempty检查！
	@Min(0)
	@Column(nullable = false) 
	private int num;
	
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)	
	@JoinColumn(name="shiftgroup_id")//设置在type表中的关联字段(外键)
	@JsonIgnore
	private ShiftGroup shiftgroup;
	
	protected Shift() { // 无参构造函数;设为 protected 防止直接使用
	}

	
	
	public Shift(String name,
			String starttime,
			String endtime,
			int num, ShiftGroup shiftgroup) {
		super();
		this.name = name;
		this.starttime = starttime;
		this.endtime = endtime;
		this.num = num;
		this.shiftgroup = shiftgroup;
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

	public void setName(String name) {
		this.name = name;
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

	

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}



	public ShiftGroup getShiftgroup() {
		return shiftgroup;
	}



	public void setShiftgroup(ShiftGroup shiftgroup) {
		this.shiftgroup = shiftgroup;
	}

	@Override
	public String toString(){
		return id+" "+name+" "+starttime+" "+endtime+" "+num;
	}


	
	
}