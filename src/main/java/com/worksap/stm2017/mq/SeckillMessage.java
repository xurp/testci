package com.worksap.stm2017.mq;

import java.io.Serializable;

public class SeckillMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	
	public SeckillMessage(Long id, String name) {
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
