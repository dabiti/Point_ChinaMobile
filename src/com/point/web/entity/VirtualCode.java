package com.point.web.entity;

/**
 * @Title: 虚拟码实体类
 * @Description:虚拟码实体类
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
public class VirtualCode {

	private String orderId;

	// 虚拟码卡号
	private String virtualCode;

	// 虚拟码卡密
	private String virtualCodePass;

	// 状态
	private String status;

	// 生成时间
	private String createTime;

	// 消费唯一标识
	private String useId;

	// 消费金额
	private String useAmount;

	// 消费内容
	private String useContent;

	// 消费时间
	private String useDatetime;

	
	// 有效期
	private String validityTime;
		
	// 提货信息
	private String deliveryMsg;
	
	// 短信模版
	private String templateId;
	
	@Override
	public String toString() {
		return "Order [orderid=" + orderId + ", virtualCode=" + virtualCode
				+ ", virtualCodePass=" + virtualCodePass + ", status=" + status
				+ ", createTime=" + createTime + ", useId=" + useId
				+ ", useAmount=" + useAmount + ", useContent=" + useContent
				+ ", useDatetime=" + useDatetime 
				+ ", validityTime=" + validityTime 
				+ ", deliveryMsg=" + deliveryMsg 
				+ ", templateId=" + templateId 
				+ "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getVirtualCode() {
		return virtualCode;
	}

	public void setVirtualCode(String virtualCode) {
		this.virtualCode = virtualCode;
	}

	public String getVirtualCodePass() {
		return virtualCodePass;
	}

	public void setVirtualCodePass(String virtualCodePass) {
		this.virtualCodePass = virtualCodePass;
	}

	public String getUseId() {
		return useId;
	}

	public void setUseId(String useId) {
		this.useId = useId;
	}

	public String getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(String useAmount) {
		this.useAmount = useAmount;
	}

	public String getUseContent() {
		return useContent;
	}

	public void setUseContent(String useContent) {
		this.useContent = useContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUseDatetime() {
		return useDatetime;
	}

	public void setUseDatetime(String useDatetime) {
		this.useDatetime = useDatetime;
	}

	public String getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(String validityTime) {
		this.validityTime = validityTime;
	}

	public String getDeliveryMsg() {
		return deliveryMsg;
	}

	public void setDeliveryMsg(String deliveryMsg) {
		this.deliveryMsg = deliveryMsg;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	
	
	
}
