package com.point.web.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;


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

	private String orderId ;
	private String phone ;
	private String itemId ;
	private String title ;
	private String price ;
	private String quantity ;
	private String finalFee ;
	private String discount ;
	private Date createTime ;
	
			
	public Order(){
		super();
	}
	
	public Order(String orderId,String phone,String itemid,String title,String price,String quantity,String finalFee,String discount){
		this.orderId = orderId;
		this.phone = phone;
		this.itemId = itemid;
		this.title = title;
		this.price = price;
		this.quantity = quantity;
		this.finalFee = finalFee;
		this.discount = discount;
	}


	@Override
	public String toString() {
		return "Order [orderId=" + orderId
				+ ", phone=" + phone + ", itemId="+ itemId 
				+ ", title=" + title + ", price="+ price 
				+ ", quantity=" + quantity + ", finalFee="+ finalFee 
				+ ", discount=" + discount + ", createTime=" + createTime + "]";
	}
	

	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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


	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getFinalFee() {
		return finalFee;
	}

	public void setFinalFee(String finalFee) {
		this.finalFee = finalFee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	

	
	

	
}
