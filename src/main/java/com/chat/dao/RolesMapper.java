package com.chat.dao;

import java.util.List;

import com.chat.entity.Roles;

public interface RolesMapper {
    int deleteByPrimaryKey(String id);

    int insert(Roles record);

    int insertSelective(Roles record);

    Roles selectByPrimaryKey(String id);
    
    List<Roles> selectUrlAndRolesByUserId(String id);
    
    int updateByPrimaryKeySelective(Roles record);

    int updateByPrimaryKey(Roles record);
}