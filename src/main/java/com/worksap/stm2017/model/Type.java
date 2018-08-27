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
public class Type{
	
	private static final long serialVersionUID = 1L;

	
	@Id // 主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 自增策略
	private Long id; // 实体一个唯一标识
	
	@NotEmpty(message = "type name can not be empty")
	@Size(min=2, max=20,message="size of name should between 2 to 20")
	@Column(nullable = false, length = 20, unique = true) 
	private String name;

	/*@Column(nullable=false)
	@CreationTimestamp
	private Timestamp createTime;*/
	
	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "type_user", joinColumns = @JoinColumn(name = "type_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))*/
	//删除type不能删除user，所以用merge
	@OneToMany(mappedBy = "type",cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
	private List<User> users;
	//blog to user is "one-to-one":one blog belongs to one user
	//but one type can have several users
	/*@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	*/
	
	protected Type() { // 无参构造函数;设为 protected 防止直接使用
	}
    
	
	
	public Type(String name) {
		this.name = name;
	}
    //一方新增两个控制多方的方法
	public void addUser(User user) {
		this.users.add(user); 
	}
	public void removeUser(Long userId) { 
		for (int index=0; index < this.users.size(); index ++ ) { 
			if (users.get(index).getId() == userId) { 
				this.users.remove(index); 
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
}