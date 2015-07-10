/**   
* @Title: PassGetback.java 
* @Package com.point.web.entity 
* @Description: TODO
* @author guowanyu   
* @date 2015年7月9日 上午10:24:17 
* @version V1.0   
*/
package com.point.web.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * @title :
 * @author : 
 * @since：2015年7月9日 上午10:24:17 
 * @Description:
 */
public class PassGetback implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String validcode;
	private Date createtime;
	private String remark;
	private String account;
	private String phone;
	private Date validtime;
	private Date newpasstime;
	private String shaid;
	private String isvald;
	
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
	public String getIsvald() {
		return isvald;
	}
	public void setIsvald(String isvald) {
		this.isvald = isvald;
	}
}
