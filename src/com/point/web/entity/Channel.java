package com.point.web.entity;

import org.apache.ibatis.type.Alias;

/**
 * @Title: Channel实体类
 * @Description:渠道商实体类
 * @Since: 2015年7月01日
 * @author liuhao
 *
 */
@Alias("Channel")
public class Channel {
	private String id;
	private String name;//渠道商名称
	private String email;//渠道商邮箱
	private String phone;//联系电话
	private String cno;//渠道商商户号
	private String password;//渠道商密码
	private String createtime;//渠道商密码
	private String status;//渠道商密码
	public Channel() {
		super();
	}
	public Channel(String id, String name, String email, String phone,
			String cno, String password, String createtime, String status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.cno = cno;
		this.password = password;
		this.createtime = createtime;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
