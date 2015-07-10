package com.point.web.entity; 
/**
 * @title :密码找回类
 * @author : 刘浩
 * @since：2015年7月9日 下午1:11:39 
 * @Description:
 */
public class PassGetBack {
	private String id;
	private String validcode;//验证码
	private String createtime;//验证码创建时间
	private String remark;//备注
	private String account;//账号
	private String phone;//手机号
	private String validtime;//密码找回验证码验证时间
	private String newpasstime;//新密码确认时间
	public PassGetBack() {
		super();
	}
	public PassGetBack(String id, String validcode, String createtime,
			String remark, String account, String phone, String validtime,
			String newpasstime) {
		super();
		this.id = id;
		this.validcode = validcode;
		this.createtime = createtime;
		this.remark = remark;
		this.account = account;
		this.phone = phone;
		this.validtime = validtime;
		this.newpasstime = newpasstime;
	}
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
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
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
	public String getValidtime() {
		return validtime;
	}
	public void setValidtime(String validtime) {
		this.validtime = validtime;
	}
	public String getNewpasstime() {
		return newpasstime;
	}
	public void setNewpasstime(String newpasstime) {
		this.newpasstime = newpasstime;
	}
	
	
}
 