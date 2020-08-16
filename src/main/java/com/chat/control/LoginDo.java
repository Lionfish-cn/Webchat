package com.chat.control;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.chat.entity.Login;
import com.chat.service.impl.LoginService;
import com.util.ArrayUtil;
import com.util.Base64Util;
import com.util.EntityUtil;

@Controller
@RequestMapping("/loginDo")
public class LoginDo{
	
	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private LoginService loginService;
	
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
					return "redirect:chat-gui";
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
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/searchUser")
	public String searchUser(HttpServletRequest request,HttpServletResponse response) {
		try {
			
			log.info("== start query user by username on ajax==");
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			String curUsername = request.getParameter("curUsername");
			Login l = loginService.selectByUsername(curUsername);
			if (l != null) {
				List<Login> ls = new ArrayList<Login>();
				ls.add(l);
				List<String> args = new ArrayList<String>();
				args.add("tUsername");
				args.add("tPassword");
				args.add("tNickName");
				args.add("tEmail");
				args.add("tAddr");
				args.add("Id");
				List<Map> rLogins = EntityUtil.entityListToMap(ls, args);
				
				if(!ArrayUtil.isEmpty(rLogins)) {
					Map m = rLogins.get(0);
					PrintWriter out = response.getWriter();
					out.write(JSONObject.toJSONString(m));
					out.flush();
					out.close();
				}
			}
			log.info("== end query user by username on ajax==");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/outLogin")
	public String outlogin(Login login) {
		return "success";
	}
	
	@RequestMapping("/update")
	public String update(Login login) {
		int i = loginService.updateByPrimaryKeySelective(login);
		if(i>0) {
			return "chat-gui";
		}
		return "chat-gui";
	}
}
