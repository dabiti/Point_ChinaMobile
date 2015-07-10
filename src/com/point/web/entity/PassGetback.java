package com.point.web.entity; 

import java.io.Serializable;
import java.util.Date;

/**
 * @title :密码找回类
 * @author : 刘浩
 * @since：2015年7月9日 下午1:11:39 
 * @Description:
 */
public class PassGetback implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String validcode;//验证码
	private Date createtime;//验证码创建时间
	private String remark;//备注
	private String account;//账号
	private String phone;//手机号
	private Date validtime;//密码找回验证码验证时间
	private Date newpasstime;//新密码确认时间
	private String shaid;
	private String isvalid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValidcode() {
		return validcode;
	}
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getValidtime() {
		return validtime;
	}
	public void setValidtime(Date validtime) {
		this.validtime = validtime;
	}
	public Date getNewpasstime() {
		return newpasstime;
	}
	public void setNewpasstime(Date newpasstime) {
		this.newpasstime = newpasstime;
	}
	public String getShaid() {
		return shaid;
	}
	public void setShaid(String shaid) {
		this.shaid = shaid;
	}
	public String getIsvalid() {
		return isvalid;
	}
	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}
	
	
	
}
 