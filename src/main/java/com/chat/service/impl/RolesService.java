package com.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.dao.RolesMapper;
import com.chat.entity.Roles;
import com.chat.service.IRolesService;

@Service
public class RolesService implements IRolesService {

	@Autowired
	private RolesMapper iRolesDao;
	
	public int deleteByPrimaryKey(String id) {
		return iRolesDao.deleteByPrimaryKey(id);
	}

	public int insert(Roles record) {
		return iRolesDao.insert(record);
	}

	public int insertSelective(Roles record) {
		return iRolesDao.insertSelective(record);
	}

	public Roles selectByPrimaryKey(String id) {
		return iRolesDao.selectByPrimaryKey(id);
	}

	public List<Roles> selectUrlAndRolesByUserId(String id) {
		return iRolesDao.selectUrlAndRolesByUserId(id);
	}

	public int updateByPrimaryKeySelective(Roles record) {
		return iRolesDao.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(Roles record) {
		return iRolesDao.updateByPrimaryKey(record);
	}
}
