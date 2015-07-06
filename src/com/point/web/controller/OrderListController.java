package com.point.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.entity.Order;
import com.point.web.entity.OrderTableEntity;
import com.point.web.service.OrderService;

/**
 * 订单详情
 * @author wangchao_b
 *
 */
@Controller
public class OrderListController {
	double total = 0.00f;
	SimpleDateFormat sdf;
	@Resource
	private OrderService orderService;
	
	@RequestMapping("/showdemolist")
	public String showOrderList(HttpServletRequest request,
			HttpServletResponse response){		
		
		return "orderList";
	}
	
	/**
	 * 订单列表处理主体业务逻辑
	 * @param request
	 * @param response
	 * @param title 
	 * @param phone
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listHandle")
	public String listHandler(HttpServletRequest request,
			HttpServletResponse response,String title,String phone,String startdate,String enddate){
		String serchDate = startdate != null?startdate:this.DateBefore();
		Map serchterms = new HashMap();
		Date date = null;
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(serchDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 缺省请求前一天数据
		if(title == null && phone == null && startdate == null && enddate == null){
			serchterms.put("startdate", date);
		}else{
			serchterms.put("startdate", date);
			serchterms.put("enddate",enddate);
			serchterms.put("phone", phone);
		}
		List<Order> orders = orderService.findByCreateDate(serchterms);
		if(orders.size() < 1) 
			return "";
		return JSONObject.fromObject(combinationRequest(orders)).toString();
	}
	
	/**
	 * 封装table容器数据生成过程
	 * @return OrderTableEntity
	 */
	private Map<String,Object> combinationRequest(List<Order> order){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<OrderTableEntity> tabledata = new ArrayList<OrderTableEntity>();
		OrderTableEntity orderTableEntity = null;
		String datestr = "";
		for(Order orderItem : order){
			orderTableEntity = new OrderTableEntity();
			datestr = sdf.format(orderItem.getCreatetime());
			orderTableEntity.setOrderid(orderItem.getOrderid());
			orderTableEntity.setPhone(orderItem.getPhone());
			orderTableEntity.setPrice(orderItem.getPrice());
			orderTableEntity.setTitle(orderItem.getTitle());
			orderTableEntity.setItemid(orderItem.getItemid());
			orderTableEntity.setFinalfee(orderItem.getFinalfee());//积分金额
			orderTableEntity.setQuantity(orderItem.getQuantity());//商品数量
			orderTableEntity.setOrderTime(datestr);
			
			total += Double.parseDouble(orderTableEntity.getPrice());
			orderTableEntity.setTotal(total+"");
			tabledata.add(orderTableEntity);
			
		}
		dataMap.put("rows", tabledata);
		if(tabledata.size() < 1)
			return null;
		dataMap.put("total", tabledata.get(tabledata.size()-1).getTotal());
		
		return dataMap;
	}
	
	/**
	 * 获取前一天日期
	 * @return
	 */
	private String DateBefore(){
		Date dNow = new Date();
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1); 
		dBefore = calendar.getTime(); 

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String defaultStartDate = sdf.format(dBefore); 

		return defaultStartDate;
	}
	
	@ResponseBody
	@RequestMapping("/getTypes")
	public String demoListGetTypes() {
		return "[{\"id\":\"1\",\"text\":\"钢化膜\"},{\"id\":\"2\",\"text\":\"高透膜\"}]";
	}
	

}
