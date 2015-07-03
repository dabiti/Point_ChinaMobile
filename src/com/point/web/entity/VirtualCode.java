package com.point.web.entity;

import java.util.Date;



public class VirtualCode {

	private String orderid;
	
	//虚拟码卡号
	private String vcode;
	
	//状态
	private String status;
	
	
	private Date createtime;
	
	@Override
	public String toString() {
		return "Order [orderid=" + orderid
				+ ", vcode=" + vcode + ", status=" + status + ", createtime=" + createtime + "]";
	}
	
	
	
	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}



	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	
		
}
