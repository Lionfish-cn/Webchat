package com.chat.control;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chat.entity.Login;
import com.chat.service.impl.ClientService;
import com.chat.service.impl.LoginService;
import com.chat.util.Base64Util;

@Controller
@RequestMapping("/loginDo")
public class LoginDo {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ClientService clientService;
	
	@RequestMapping("/login")
	public String login(Login login,HttpServletRequest request) {
		String tUsername = login.gettUsername();
		String tPassword = login.gettPassword();
		tPassword = Base64Util.base64Encoder(tPassword);
		Login sLogin = loginService.selectByUsername(tUsername);
		if(sLogin!=null){
			
			try {
				Subject subject =  SecurityUtils.getSubject();
					SecurityUtils.getSecurityManager().logout(subject);
					UsernamePasswordToken passwordToken = new UsernamePasswordToken(tUsername,tPassword);
					subject.login(passwordToken);
					int i = loginService.updateByPrimaryKey(sLogin);
					if(i>0) {
						log.info("修改t_login表成功，修改字段为t_ip");
						return "redirect:chat-gui";
					}else {
						log.error("修改失败！请检查代码！");
					}
					/*
					 * if(!subject.isAuthenticated()) { }else {
					 * request.setAttribute("error","该用户已在线！"); }
					 */
				
				
				
			} catch (Exception e) {
				request.setAttribute("error", "账号密码错误");
				e.printStackTrace();
			}
		}else {
			request.setAttribute("error","账号不存在，请先去注册账号！");
		}
		
		return "forward:/index.jsp";
	}
	
	@RequestMapping("/chat-gui")
	public String  chatgui(){
		return "chat-gui";
	}
	
	@RequestMapping("/outLogin")
	public String outlogin(Login login) {
		return "success";
	}
}
