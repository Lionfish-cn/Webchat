package com.chat.service;

import com.chat.entity.Login;

public interface ILoginService {
	Login selectByUsername(String tUsername);
	
	Login selectByPrimaryKey(String id);
	
	int updateByPrimaryKey(Login record);
	
	int updateByPrimaryKeySelective(Login record);
	
	int insert(Login record);
	
	
}
