package com.worksap.stm2017.model;


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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Type 实体.
 */
@Entity // 实体
public class ShiftGroup{
	
	private static final long serialVersionUID = 1L;

	
	@Id // 主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 自增策略
	private Long id; // 实体一个唯一标识
	
	@NotEmpty(message = "shift group name can not be empty")
	@Size(min=2, max=50,message="size of name should between 2 to 50")
	@Column(nullable = false, length = 50, unique = true) 
	private String name;

	//这里用了all，删除班次组则删除所有班次
	@OneToMany(mappedBy = "shiftgroup",cascade=CascadeType.ALL)
	private List<Shift> shifts;
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="type_id")
	private Type type;//now, a type can have several groups. But in type, onetoone is not defined. So one to one here is ok because a shiftgroup has one type.
	
	
	protected ShiftGroup() { // 无参构造函数;设为 protected 防止直接使用
	}
    
	
	
	public ShiftGroup(String name) {
		this.name = name;
	}
    //一方新增两个控制多方的方法
	public void addShift(Shift shift) {
		this.shifts.add(shift); 
	}
	public void removeShift(Long shiftId) { 
		for (int index=0; index < this.shifts.size(); index ++ ) { 
			if (shifts.get(index).getId() == shiftId) { 
				this.shifts.remove(index); 
				break; 
				} 
			}  
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



	public List<Shift> getShifts() {
		return shifts;
	}



	public void setShifts(List<Shift> shifts) {
		this.shifts = shifts;
	}



	public Type getType() {
		return type;
	}



	public void setType(Type type) {
		this.type = type;
	}	

	
	
	
}