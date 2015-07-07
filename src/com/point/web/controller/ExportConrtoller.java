package com.point.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.point.web.service.DownloadExcelServlet;



@Controller

public class ExportConrtoller {
	@Resource
	private DownloadExcelServlet downloadExcelServlet;


	private static Logger log = Logger.getLogger(ExportConrtoller.class);
	
	
	@RequestMapping("/export/order")
	
	public String exportOrder2excel(HttpServletRequest request,
			HttpServletResponse response) {
			
		return "export_order";
	}
	
	
	@RequestMapping("printorder")
	public void printOrder(HttpSession session, HttpServletResponse response){
	
		//export_order.print_order_Format();		
	}
	
	@RequestMapping("DownloadExcelServlet")
	public void downloadExcelServlet(HttpSession session, HttpServletResponse response,HttpServletRequest request) throws ServletException, IOException
	{
		downloadExcelServlet.downLoad(request, response);
	}
	
	
	
}


