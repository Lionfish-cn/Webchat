package com.chat.filter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

public class ChatLogoutFilter extends LogoutFilter{

	/**
	 *
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		String redirectUrl = getRedirectUrl(request, response, subject);
		ServletContext context = request.getServletContext();
		try {
			subject.logout();
			context.removeAttribute("error");
			issueRedirect(request, response, redirectUrl);;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
}
