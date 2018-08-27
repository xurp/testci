package com.worksap.stm2017.model;


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
import javax.persistence.Table;
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
@Table(name="user", schema = "xu_xi_rd2")
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id // 主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 自增策略
	private Long id; // 实体一个唯一标识
	
	@NotEmpty(message = "name can not be empty")
	@Size(min=2, max=20,message="size of name should between 2 to 20")
	@Column(nullable = false, length = 20, unique = true) // 映射为字段，值不能为空
	private String name;

	@NotEmpty(message = "email can not be empty")
	@Size(max=50)
	@Email(message= "email format is wrong" ) 
	@Column(nullable = false, length = 50)
	private String email;
	
	@NotEmpty(message = "username can not be empty")
	@Size(min=3, max=20,message="size of username should between 3 to 20")
	@Column(nullable = false, length = 20, unique = true)
	private String username; // 用户账号，用户登录时的唯一标识

	@NotEmpty(message = "password can not be empty")
	@Size(max=100)
	@Column(length = 100)
	private String password; // 登录时密码
	
	/*@NotEmpty(message = "职务不能为空")
	@Size(max=100)
	@Column(length = 100)
	private String role; // 职务*/
	
	//可选属性optional=false,表示type不能为空。这里用了merge+refresh
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)	
	@JoinColumn(name="type_id")//设置在type表中的关联字段(外键)
	@JsonIgnore
	private Type type;
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;
	
	protected User() { // 无参构造函数;设为 protected 防止直接使用
	}
	//是否要加type存疑
	public User(String name, String email,String username,String password,Type type) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.type=type;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	/*public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}*/

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//  需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
		List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
		for(GrantedAuthority authority : this.authorities){
			simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}
		return simpleAuthorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("User[id=%d,name='%s',username='%s']", id, name, username);
	}
}


/*
public class User {
	private Integer id;
	private String name;
	private Integer age;
	private String address;
	
	public User() {
		
	}

	public User(Integer id, String name, Integer age, String address) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", address=" + address + "]";
	}
	
}
*/