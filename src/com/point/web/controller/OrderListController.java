package com.point.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.entity.Channel;
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
			HttpServletResponse response,String title,String phone,String startdate,String enddate,String page,String rows ){
		String serchDate = startdate != null?startdate:this.DateBefore();
		Map serchterms = new HashMap();
		Date date = null;
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		/****/
		String top=(Integer.parseInt(page)-1)*Integer.parseInt(rows)+1+"";//top为分页的第一条记录
		String bottom=Integer.parseInt(page)*Integer.parseInt(rows)+"";//bottom为分页的最后一条记录
		/****/	
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
			serchterms.put("top", top);
			serchterms.put("bottom", bottom);
		List<Order> orders = orderService.findByCreateDate(serchterms);
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
		double total = 0.00f;
		String datestr = "";
		for(Order orderItem : order){
			orderTableEntity = new OrderTableEntity();
			datestr = sdf.format(orderItem.getCreateTime());
			orderTableEntity.setOrderid(orderItem.getOrderId());
			orderTableEntity.setPhone(orderItem.getPhone());
			orderTableEntity.setPrice(orderItem.getPrice());
			orderTableEntity.setTitle(orderItem.getTitle());
			orderTableEntity.setItemid(orderItem.getItemId());
			orderTableEntity.setFinalfee(orderItem.getFinalFee());//积分金额
			orderTableEntity.setQuantity(orderItem.getQuantity());//商品数量
			orderTableEntity.setOrderTime(datestr);
			
			total += Double.parseDouble(orderTableEntity.getPrice());
			orderTableEntity.setTotal(total+"");
			tabledata.add(orderTableEntity);
			
		}
		dataMap.put("rows", tabledata);
		dataMap.put("total", tabledata.size());
		dataMap.put("mytotal", tabledata.size() < 1 ? 0 : tabledata.get(tabledata.size()-1).getTotal());
		
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
	

	@RequestMapping("exportOrder")
	public void exportChannel(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
response.setCharacterEncoding("utf-8");

String phone=request.getParameter("phone");
String startdate=request.getParameter("startDate");
String enddate=request.getParameter("endDate");
		String fileName ="order";//设置导出的文件名称
		HashMap<String, String> map = new HashMap<String, String>();// map传入多个参数•page: 页号，从1计起。 •rows: 每页记录大小。 
		map.put("phone", phone);
		map.put("startdate", startdate);
		map.put("enddate", enddate);
	
		
		 List<Order> orders= orderService.findByCreateDate(map);
		 
		 String strOrder="";
		 strOrder+="<table border='1'>";
		 strOrder+="<tr><td>订单号</td><td>用户手机号</td><td>商品名称</td><td>商品单价</td><td>商品数量</td><td>创建日期</td></tr>";
		 
		 for(Order order:orders){
			 strOrder+="<tr><td>"+order.getOrderId()+"</td><td>"+order.getPhone()+"</td><td>"+order.getTitle()+"</td><td>"+order.getPrice()+"</td><td>"+order.getQuantity()+"</td><td>"+order.getCreateTime().toString()+"</td></tr>";
			 
			 
		 }
		 //System.out.println(strchannel);
	        
	        StringBuffer sb = new StringBuffer(strOrder);//将表格信息放入内存
	        
	        
	        String contentType ="application/vnd.ms-excel";//定义导出文件的格式的字符串
	        String recommendedName = new String(fileName.getBytes(),"ISO-8859-1");//设置文件名称的编码格式
	        response.setContentType(contentType);//设置导出文件格式
	        response.setHeader("Content-Disposition", "attachment; filename=" + "订单导出" + ".XLS");//
	        response.resetBuffer();
	        //利用输出输入流导出文件
	        ServletOutputStream sos = response.getOutputStream();
	        sos.write(sb.toString().getBytes());
	        sos.flush();
	        sos.close();
	        
	}
	
	
}
