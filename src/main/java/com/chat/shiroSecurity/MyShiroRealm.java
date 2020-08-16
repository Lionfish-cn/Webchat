package com.chat.shiroSecurity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.chat.entity.Login;
import com.chat.entity.Roles;
import com.chat.service.impl.LoginService;
import com.chat.service.impl.RolesService;
import com.util.StringUtil;

public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private RolesService rolesService;
	
	/**
	 *	配置用户授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//获取用户名
		Login principal = (Login)principals.getPrimaryPrincipal();
		//获取用户角色
		Set<String> roles;
		Set<String> objectPermissions;
		try {
			if(principal!=null) {
				String  username = principal.gettUsername();
				roles = getRolesByUserName(username);
				objectPermissions = getPermissionsByUserName(username);
				
				SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
				sai.setStringPermissions(objectPermissions);
				sai.setRoles(roles);
				return sai;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 	获取用户授权
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String principal = (String)token.getPrincipal();
		Login l;
		try {
			if(StringUtil.isNotNull(principal)) {
				l = getLoginByUsername(principal);
				if(l!=null) {
					SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(l, l.gettPassword(), getName());
					authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(principal));
					return authenticationInfo;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Set<String> getPermissionsByUserName(String username) throws Exception{
		Login login = getLoginByUsername(username);
		if(login!=null) {
			String id = login.getId();
			if(id!=null) {
				List<Roles> roles = rolesService.selectUrlAndRolesByUserId(id);
				Set<String> s = new HashSet<String>();
				for (Roles role : roles) {
					if(StringUtil.isNotNull(role.gettRoleUrl())) {
						s.add(role.gettRoleUrl());
					}
				}
				return s;
			}
		}
		return null;
	} 
	
	private Set<String> getRolesByUserName(String username) throws Exception{
		Login login = getLoginByUsername(username);
		if(login!=null) {
			String id = login.getId();
			if(id!=null) {
				List<Roles> roles = rolesService.selectUrlAndRolesByUserId(id);
				Set<String> s = new HashSet<String>();
				for (Roles role : roles) {
					if(StringUtil.isNotNull(role.gettRole())) {
						s.add(role.gettRole());
					}
				}
				return s;
			}
		}
		return null;
	}
	
	private Login getLoginByUsername(String username) throws Exception{
		Login login = loginService.selectByUsername(username);	
		return login;
	}

}
