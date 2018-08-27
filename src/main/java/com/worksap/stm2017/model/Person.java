package com.worksap.stm2017.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

//@Data
@Entity
public class Person {

    /*private Integer personId;
    private String firstName;
    private String lastName;
    private String address;
    private String city;*/
	private static final long serialVersionUID = 1L;

	@Id // 主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 自增策略
	private Long id;
	@Column(nullable = false)
	private String name;
	public Person() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Person(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	

}
