package com.point.web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import com.point.web.util.PassEncodeTool;
/**
 * @Title:进行密码匹配的类
 * @Description:登录后进行密码匹配的类
 * @Since: 2015年7月06日上午11:09:20
 * @author guowanyu
 */
public class QxShiroCredentialsMatcher extends HashedCredentialsMatcher {
	@Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
		this.setHashAlgorithmName(PassEncodeTool.ALGORITHM_NAME);
		this.setHashIterations(PassEncodeTool.HASH_ITERATIONS);
		this.setStoredCredentialsHexEncoded(true);
        return super.doCredentialsMatch(authcToken, info);
    }
}
