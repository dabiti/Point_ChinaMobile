package com.point.web.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("Resource")
public class Resource implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String resname;
	private String resurl;
	private String pid;
	private String resno;
	private String resperm;
	private String restype;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResname() {
		return resname;
	}
	public void setResname(String resname) {
		this.resname = resname;
	}
	public String getResurl() {
		return resurl;
	}
	public void setResurl(String resurl) {
		this.resurl = resurl;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getResno() {
		return resno;
	}
	public void setResno(String resno) {
		this.resno = resno;
	}
	public String getResperm() {
		return resperm;
	}
	public void setResperm(String resperm) {
		this.resperm = resperm;
	}
	public String getRestype() {
		return restype;
	}
	public void setRestype(String restype) {
		this.restype = restype;
	}
	
}
