package com.chat.service;

import java.util.List;

import com.chat.entity.Roles;

public interface IRolesService {
	int deleteByPrimaryKey(String id);

    int insert(Roles record);

    int insertSelective(Roles record);

    Roles selectByPrimaryKey(String id);
    
    List<Roles> selectUrlAndRolesByUserId(String id);
    
    int updateByPrimaryKeySelective(Roles record);

    int updateByPrimaryKey(Roles record);
}
