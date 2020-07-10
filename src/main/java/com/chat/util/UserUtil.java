package com.chat.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.chat.entity.Login;

public class UserUtil {
	public static Login getUser() {
		Subject subject = SecurityUtils.getSubject();
		return (Login)subject.getPrincipal();
	}
}
