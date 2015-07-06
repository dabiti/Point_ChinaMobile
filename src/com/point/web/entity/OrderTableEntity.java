package com.point.web.entity;

import java.util.Date;

/**
 * 用于显示在界面上的实体类
 * @author wangchao_b
 *
 */
public class OrderTableEntity {

	String ordertime = "";
	String total = "";
	String orderid = "";
	String phone = "";
	String itemid = "";
	String title = "";
	String price = "";
	String quantity = "";
	/**
	 * 积分金额
	 */
	String finalfee = "";
	/**
	 * 商品数量
	 */
	String discount = "";
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
	public String getOrderTime() {
		return ordertime;
	}
	public void setOrderTime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}

}
