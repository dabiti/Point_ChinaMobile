package com.point.web.shiro;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.point.web.dao.ResourceDao;
import com.point.web.entity.User;
import com.point.web.service.UserService;
import com.point.web.util.PassEncodeTool;
/**
 * @Title:权限管理认证与授权类
 * @Description:写入了认证与授权的方法
 * @Since: 2015年7月06日上午11:07:30
 * @author guowanyu
 */
public class QxShiroRealm extends AuthorizingRealm {
      @Resource
	  private UserService userService;
      
      @Resource
      private ResourceDao resourceDao;
      
	  @Override
	  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	    // 根据用户配置用户与权限
	    if (principals == null) {
	      throw new AuthorizationException("");
	    }
	    String name = (String) getAvailablePrincipal(principals);
	    
	    List<String> permissions = new ArrayList<String>();
	    
	    User user = userService.getByUsername(name);
	    
	    if (user.getUsername().equals(name)) {
	    	List<com.point.web.entity.Resource> resList = this.resourceDao.getAllUrlResourceByUsername(name);
	    	for(com.point.web.entity.Resource resource : resList){
	    		if(!permissions.contains(resource.getResurl())){
	    			permissions.add(resource.getResurl());
	    		}
	    	}
	    } else {
	      throw new AuthorizationException();
	    }
	    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	    
	    info.addStringPermissions(permissions);
	    
	    return info;
	  }

	  @Override
	  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
	      throws AuthenticationException {
	    UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
	    User user = userService.getByUsername(token.getUsername());
	    if (user == null) {
	    	throw new AuthenticationException();
	    }
	    SimpleAuthenticationInfo info = null;
	    if (user.getUsername().equals(token.getUsername())) {
	    	info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
	    	info.setCredentialsSalt(ByteSource.Util.bytes(PassEncodeTool.constructSalt(user.getUsername(), user.getSalt())));
	    }
	    return info;
	  }

}
