package com.point.web.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

/**
 * 
 * @Title: Order实体类
 * @Description:
 * @Author: wcl
 * @Since: 2015年5月20日
 * @Version: 1.1.0
 */
@Alias("Order")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	private String orderid = "";
	private String phone = "";
	private String itemid = "";
	private String title = "";
	private String price = "";
	private String quantity = "";
	private String finalfee = "";
	private String discount = "";
	private String createdate = "";
	private String createtime = "";
	
			
	public Order(){
		super();
	}
	
	public Order(String orderid,String phone,String itemid,String title,String price,String quantity,String finalfee,String discount,String createdate,String createtime){
		this.orderid = orderid;
		this.phone = phone;
		this.itemid = itemid;
		this.title = title;
		this.price = price;
		this.quantity = quantity;
		this.finalfee = finalfee;
		this.discount = discount;
		this.createdate = createdate;
		this.createtime = createtime;
	}

	@Override
	public String toString() {
		return "Order [orderid=" + orderid
				+ ", phone=" + phone + ", itemid="+ itemid 
				+ ", title=" + title + ", price="+ price 
				+ ", quantity=" + quantity + ", finalfee="+ finalfee 
				+ ", discount=" + discount + ", createdate="+ createdate 
				+ ", createtime=" + createtime + "]";
	}
	
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getFinalfee() {
		return finalfee;
	}

	public void setFinalfee(String finalfee) {
		this.finalfee = finalfee;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
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
	
	

	
}
