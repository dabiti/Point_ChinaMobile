package com.point.web.entity;

import java.util.UUID;

import com.point.web.util.StringTool;


public class VirtualCode {

	private String orderid;
	
	//虚拟码卡号，流水号
	private String vcode;
	
	//虚拟码卡密
	private String vcodepass;

	
	//状态
	private String status;
	
	private String createdate;
	
	private String createtime;
	
	@Override
	public String toString() {
		return "Order [orderid=" + orderid
				+ ", vcode=" + vcode + ", vcodepass="+ vcodepass 
				+ ", status=" + status + ", createdate="+ createdate 
				+ ", createtime=" + createtime + "]";
	}
	
	
	public VirtualCode(){
		super();
	}
	
	public VirtualCode(String orderId, String time,String seq){
		createdate = time.substring(0, 9);
		createtime = time.substring(11);
		//卡号
		StringBuffer sb = new StringBuffer();
		sb.append("CM");
		time = time.replace(" ", "");
		time = time.replace("-", "");
		time = time.replace(":", "");
		sb.append(time);
		sb.append(seq);
		vcode = sb.toString();
		//卡密
		UUID uuid = UUID.randomUUID();
		vcodepass = StringTool.MD5_16(uuid.toString());
		status = "0";
		orderid = orderId;
	}
	
	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}



	public String getVcodepass() {
		return vcodepass;
	}

	public void setVcodepass(String vcodepass) {
		this.vcodepass = vcodepass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	
		
}
