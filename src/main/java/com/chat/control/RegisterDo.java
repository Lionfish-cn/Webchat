package com.chat.control;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chat.entity.Login;
import com.chat.entity.Roles;
import com.chat.service.impl.LoginService;
import com.chat.service.impl.RolesService;
import com.util.Base64Util;
import com.util.generate.IDGenerator;

@Controller
@RequestMapping(value="/registerDo")
public class RegisterDo {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private RolesService rolesService;
	
	@RequestMapping(value="/toRegister")
	public String toRegister() {
		return "register/register";
	}
	
	@RequestMapping(value="/register")
	public String register(Login login) {
		login.setId(IDGenerator.generatorID());
		login.settTime(new Date());
		login.settPassword(Base64Util.base64Encoder(login.gettPassword()));
		int i = loginService.insert(login);
		if(i>0) {
			Roles roles  = new Roles();
			roles.setId(IDGenerator.generatorID());
			roles.settRoleUrl("/chatDo/toChatGui");
			roles.settUserId(login.getId());
			roles.settTime(new Date());
			rolesService.insert(roles);
		}
		return "redirect:/index.jsp";
	}
	

	
	@RequestMapping("/toJudgeExist")
	public void judgeIsExist(String username,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			Login isExist = loginService.selectByUsername(username);
			PrintWriter out = response.getWriter();	
			if(isExist!=null) {
				out.write("该用户名已经存在！");
			}else {
				out.write("");
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
