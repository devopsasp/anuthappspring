package com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Role {
	
	@Id
	@GeneratedValue
	private int id;
	
	String roleName;
 
	@ManyToOne
	User user;
	
	public Role()
	   { 
		   
	   }
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getRoleName() {
	return roleName;
}
public void setRoleName(String roleName) {
	this.roleName = roleName;
}

@Override
public String toString() {
	return "Role [id=" + id + ", roleName=" + roleName + ", user=" + user + "]";
}
   


}
