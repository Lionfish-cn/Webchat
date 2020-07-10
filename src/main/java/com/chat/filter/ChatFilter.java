package com.chat.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;

import com.chat.util.StringUtil;

public class ChatFilter extends AbstractSecurityInterceptor implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest  request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			String type = request.getParameter("opt");
			
			String username = request.getParameter("username");
			if(StringUtil.isNotNull(username)) {
				if("login".equals(type)) {//判断访问是否是登录
					Boolean isAlive = new Thread(username).isAlive();
					if(isAlive) {//判断当前用户是否存在
						//存在
						//response.sendRedirect(request.getContextPath()+"/index.jsp");
						request.setAttribute("msg", username+"已经在线！");
					}
				}
			}else {
				//response.sendRedirect(request.getContextPath()+"/index.jsp");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		
	}

	@Override
	public Class<?> getSecureObjectClass() {
		return null;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return null;
	}

}
