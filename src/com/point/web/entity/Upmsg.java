package com.point.web.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 
 * @Title: Order实体类
 * @Description:
 * @Author: wcl
 * @Since: 2015年5月20日
 * @Version: 1.1.0
 */
@Alias("Upmsg")
public class Upmsg implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;
	private String phone;
	private String upTime;
	private String port;
	private String content;
	
	private String status;

	public Upmsg() {
		super();
	}

	public Upmsg(String userId, String phone, String upTime, String port,
			String content,String status) {
		this.userId = userId;
		this.phone = phone;
		this.upTime = upTime;
		this.port = port;
		this.content = content;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Upmsg [userId=" + userId + ", phone=" + phone + ", upTime="
				+ upTime + ", port=" + port + ", content=" + content +", status=" + status + "]";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
