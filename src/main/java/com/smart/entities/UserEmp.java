package com.smart.entities;


import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="USEREMP")
public class UserEmp {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int empid;
	
	@NotBlank(message="Name is required")
	@Size(min = 2, max = 20, message = "min 2 max 20 characters allowed")
	private String name;
	
	//@NotBlank(message="Email is required")
	private String email;
	
	//@NotBlank(message="Password is required")
	//private String password;
	private String role;
	private boolean enabled;
	//private String imageUrl;
	//private String about;
	
	@ManyToOne
	private User user;
	
	
	public UserEmp() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getEmpId() {
		return empid;
	}
	public void setEmpId(int empid) {
		this.empid = empid;
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
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.empid==((UserEmp) obj).getEmpId();
	}
	
	

	/*
	 * @Override public String toString() { return "UserEmp [empid=" + empid +
	 * ", name=" + name + ", email=" + email + ", password=" + password + ", role="
	 * + role + ", enabled=" + enabled + ", user=" + user + "]"; }
	 */
	
}
