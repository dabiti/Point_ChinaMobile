package com.point.web.util;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 分页标签类
 * 
 * @author think
 * 
 */
public class PagerTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private int pageSize = 10;// 每页大小
	private int pageNo = 1;// 号码
	private int recordCount;// 记录数
	private String url;// 提交地址

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int doStartTag() throws JspException {
		int pageCount = (this.recordCount + this.pageSize - 1) / this.pageSize;

		if (this.pageNo > pageCount) {
			this.pageNo = pageCount;
		}
		if (this.pageNo < 1) {
			this.pageNo = 1;
		}

		StringBuilder sb = new StringBuilder();

		sb.append(
				"<form name='pageController' id='pageController' action='' method='post'>\r\n")
				.append("<input type='hidden' id='pageNo' name='pageNo' value='"
						+ this.pageNo + "' />\r\n");
		sb.append("</form>\r\n");

		sb.append("共" + this.recordCount + "条记录");
		sb.append("&nbsp;&nbsp;当前第" + this.pageNo + "页/\r\n").append(
				"共" + pageCount + "页&nbsp;&nbsp;");
		if (this.pageNo == 1) {
			sb.append("首页");
			sb.append("&nbsp;");
			sb.append("上一页\r\n");
		} else {
			sb.append("<a href='javascript:void(0);' onclick='turnOverPage(1)'>首页</a>\r\n");
			sb.append("&nbsp;");
			sb.append("<a href='javascript:void(0);' onclick='turnOverPage(")
					.append(this.pageNo - 1).append(")'>上一页</a>\r\n");
		}
		sb.append("&nbsp;");
		if (this.pageNo == pageCount) {
			sb.append("下一页");
			sb.append("&nbsp;");
			sb.append("尾页\r\n");
		} else {
			sb.append("<a href='javascript:void(0);' onclick='turnOverPage(")
					.append(this.pageNo + 1).append(")'>下一页</a>\r\n");
			sb.append("&nbsp;");
			sb.append("<a href='javascript:void(0);' onclick='turnOverPage(")
					.append(pageCount).append(")'>尾页</a>\r\n");
		}
		sb.append("&nbsp;转到第<input type='text' size='2' id='goPage' />页&nbsp;")
				.append("<input type='button' value='GO' onclick='judgePage(parseInt(document.getElementById(\"goPage\").value))' />");
		sb.append("\r\n");

		sb.append("<script type='text/javascript'>\r\n");
		sb.append("  //翻页函数\t\n");
		sb.append("  function turnOverPage(no){\r\n");
		sb.append("    var form = document.getElementById('pageController');\r\n");
		sb.append("    //页号越界处理\r\n");
		sb.append("    if(no").append(">").append(pageCount).append(") {\r\n");
		sb.append("        no=").append(pageCount).append(";\r\n");
		sb.append("    }\r\n");
		sb.append("    if(no").append("< 1){\r\n");
		sb.append("        no=1;\r\n");
		sb.append("    }\r\n");
		sb.append("    document.getElementById('pageNo').value=no;\r\n");
		sb.append("    form.action='").append(this.url).append("';\r\n");
		sb.append("    form.submit();\r\n");
		sb.append("  }\r\n");
		sb.append("  function  judgePage(no){\r\n");
		sb.append("    if(isNaN(no)){ alert('页码为空或输入错误！'); return; }");
		sb.append("    if(no").append(">").append(pageCount)
				.append(" || no < 1) {\r\n");
		sb.append("      alert('超出页码范围！');\r\n");
		sb.append("    }\r\n");
		sb.append("    else {\r\n");
		sb.append("      turnOverPage(no);\r\n");
		sb.append("    }\r\n");
		sb.append("  }\r\n");
		sb.append("</script>\r\n");
		try {
			this.pageContext.getOut().println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return super.doStartTag();
	}
}