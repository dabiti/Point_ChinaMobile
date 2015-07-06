package com.point.web.entity;

import org.apache.ibatis.type.Alias;

/**
 * @Title User实体类
 * @Description:
 * @author yanbin_a
 * @since 2015年6月30日
 * @version 1.1.0
 */
@Alias("User")
public class User {
	private static final long serialVersionUID = 1L;
	
	private String Id = "";
	private String username = "";
	private String password = "";
	private String realname = "";
	private String jobnumber = "";
	private String description = "";
	private String createtime = "";
	private String phone = "";
	private String email = "";
	private String enable = "";
	

	public User(){
		super();
	}
	
	public String getId() {
		return Id;
	}


	public void setId(String id) {
		Id = id;
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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getJobnumber() {
		return jobnumber;
	}

	public void setJobnumber(String jobnumber) {
		this.jobnumber = jobnumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	@Override
	public String toString() {
		return "User [userId=" + Id + ", username=" + username
				+ ", password=" + password + ", realname=" + realname
				+ ", jobnumber=" + jobnumber + ", description=" + description
				+ ", createtime=" + createtime + ", phone=" + phone
				+ ", email=" + email + ", enable=" + enable + "]";
	}
}
