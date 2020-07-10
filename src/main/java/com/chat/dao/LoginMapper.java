package com.chat.dao;

import com.chat.entity.Login;

public interface LoginMapper {
    int deleteByPrimaryKey(String id);

    int insert(Login record);

    int insertSelective(Login record);

    Login selectByPrimaryKey(String id);
    
    Login selectByUsername(String tUserName);

    int updateByPrimaryKeySelective(Login record);

    int updateByPrimaryKey(Login record);
}