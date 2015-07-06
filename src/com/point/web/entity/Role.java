package com.point.web.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("Role")
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String rolename;
	private String roledesc;
	private String rolecode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getRoledesc() {
		return roledesc;
	}
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}
	public String getRolecode() {
		return rolecode;
	}
	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	
}
