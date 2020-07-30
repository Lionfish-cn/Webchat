package com.chat.control;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chat.entity.Login;
import com.chat.service.impl.LoginService;

@RequestMapping("/avatarUploaderDo")
@Controller
public class AvatarUploaderDo {
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/uploader")
	public void uploader(@RequestParam("filename")MultipartFile file,HttpServletRequest request,HttpServletResponse response) {
		String username = request.getParameter("username");
		String root = "E:/jee-2020-06/workspace";
		String contextpath = request.getContextPath();
		String path = contextpath+ "/src/main/webapp/resource/images";
		String filename = file.getOriginalFilename();
		
		try {
			String headerUrl = root + path + "/" + username;
			
			File ff = new File(headerUrl);
			if(!ff.exists()) {
				ff.mkdirs();
			}
			
			String type = filename.substring(filename.indexOf("."));
			filename = System.currentTimeMillis()+type;
			
			File f = new File(ff, filename);
			if(!f.exists()) {
				file.transferTo(f);
			}
			
			String relativePath  = contextpath+ "/resource/images/" + username + "/"
					+ filename;
			Login login = loginService.selectByUsername(username);
			login.settImage(relativePath);
			int i = loginService.updateByPrimaryKeySelective(login);
			if(i>0) {
				PrintWriter out = response.getWriter();
				out.write(relativePath);
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
