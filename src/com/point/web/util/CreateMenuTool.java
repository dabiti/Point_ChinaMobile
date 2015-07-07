package com.point.web.util;

import java.util.List;

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.point.web.dao.ResourceDao;
import com.point.web.dao.RoleDao;
import com.point.web.entity.Resource;
import com.point.web.entity.Role;
/**
 * @Title:建立菜单类
 * @Description:根据当前登录的用户得到其所可以访问的菜单项
 * @Since: 2015年7月06日上午11:20:10
 * @author guowanyu
 */
@Component
public class CreateMenuTool {
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	public static final String menuGroup_div = "<div id=\"MenuGroup\" class=\"easyui-accordion\" data-options=\"multiple:false\" fit=\"true\" border=\"false\">";
	public static final String menuTop_div = "<div title=\"{0}\" data-options=\"iconCls:'icon-ok'\" icon=\"icon-sys\" style=\"overflow:auto;\">";
	public static final String menu_ul = "<ul style=\"padding:0;margin:4px 0 0 0;\">";
	public static final String menu_li = "<li><a  onclick=\"addTab('{0}','{1}')\"><span class=\"nav nav-checkaccount\"></span>{2}</a></li>";
	public static final String ul_end = "</ul>";
	public static final String div_end = "</div>";
	
	public String getMenu(Subject subject){
		
		String username = (String)subject.getPrincipal();
		List<Role> roles = roleDao.getRolesByUsername(username);
		Role role = roles.get(0);
		
		List<Resource> topResList = resourceDao.getTopMenuResource(role.getRolename());
		
		StringBuffer sbf = new StringBuffer();
		sbf.append(menuGroup_div);
		
		String resname=null;
		for(Resource topRes : topResList){
			sbf.append(menuTop_div.replace("{0}", topRes.getResname()));
			List<Resource> subResList = resourceDao.getSubMenuResource(topRes.getId());
			for(Resource subRes : subResList){
				sbf.append(menu_ul);
				resname = subRes.getResname();
				sbf.append(menu_li.replace("{0}", resname).replace("{1}", subRes.getResurl()).replace("{2}", resname));
				sbf.append(ul_end);
			}
			sbf.append(div_end);
		}
		sbf.append(div_end);
		
		return sbf.toString();
	}

}
