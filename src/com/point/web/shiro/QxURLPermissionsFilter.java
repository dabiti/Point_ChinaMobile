package com.point.web.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
/**
 * @Title:权限url过滤类
 * @Description:如果授权验证失败，即登录用户没有该url的访问权限，进行url过滤
 * @Since: 2015年7月06日上午11:17:35
 * @author guowanyu
 */
public class QxURLPermissionsFilter extends PermissionsAuthorizationFilter {
    public boolean isAccessAllowed(ServletRequest request,  
            ServletResponse response, Object mappedValue) throws IOException {  
         return super.isAccessAllowed(request, response, buildPermissions(request));  
    }  
    protected String[] buildPermissions(ServletRequest request) {  
        String[] perms = new String[1];  
        HttpServletRequest req = (HttpServletRequest) request;  
        String path = req.getServletPath();  
        perms[0] = path;
        return perms;  
    }  
}
