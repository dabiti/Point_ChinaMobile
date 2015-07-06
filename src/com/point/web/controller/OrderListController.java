package com.point.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.entity.Order;
import com.point.web.entity.OrderTableEntity;
import com.point.web.service.OrderService;

@Controller
public class OrderListController {
	double total = 0.00f;
	@Resource
	private OrderService orderService;
	
	@RequestMapping("/showdemolist")
	public String showOrderList(HttpServletRequest request,
			HttpServletResponse response){		
		
		return "orderList";
	}
	@ResponseBody
	@RequestMapping("/listHandle")
	public String listHandler(HttpServletRequest request,
			HttpServletResponse response,String title,String phone,String startdate,String enddate){
		if(title == null && phone == null && startdate == null && enddate == null)
			return "";
		Map serchterms = new HashMap();
		List<OrderTableEntity> tabledata = new ArrayList<OrderTableEntity>();
		Date date = null;
		OrderTableEntity orderTableEntity = null;
		String datestr = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(startdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		serchterms.put("startdate", date);
		serchterms.put("enddate",enddate);
		serchterms.put("phone", phone);
		List<Order> order = orderService.findByCreateDate(serchterms);
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
			System.out.println("t:"+total);
			tabledata.add(orderTableEntity);
			
		}
		
		
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(tabledata);
		
		return array.toString();
	}
	
	@ResponseBody
	@RequestMapping("/getTypes")
	public String demoListGetTypes() {
		return "[{\"id\":\"1\",\"text\":\"钢化膜\"},{\"id\":\"2\",\"text\":\"高透膜\"}]";
	}
	

}
