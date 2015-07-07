package com.point.web.entity;

import java.util.Date;

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

	@Override
	public String toString() {
		return "Order [orderid=" + orderId + ", virtualCode=" + virtualCode
				+ ", virtualCodePass=" + virtualCodePass + ", status=" + status
				+ ", createTime=" + createTime + ", useId=" + useId
				+ ", useAmount=" + useAmount + ", useContent=" + useContent
				+ ", useDatetime=" + useDatetime + "]";
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

}
