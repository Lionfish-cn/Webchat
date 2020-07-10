package com.chat.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chat.dao.LoginMapper;
import com.chat.entity.Login;
import com.chat.service.ILoginService;
import com.chat.util.generate.IDGenerator;

@Service
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/spring-dao.xml")
public class LoginService implements ILoginService {

	@Autowired
	private LoginMapper iLoginDao;
	
	public Login selectByUsername(String tUsername) {
		Login login = iLoginDao.selectByUsername(tUsername);
		return login;
	}

	public Login selectByPrimaryKey(String id) {
		Login login = iLoginDao.selectByPrimaryKey(id);
		return login;
	}

	public int insert(Login record) {
		int i = iLoginDao.insert(record);
		return i;
	}

	public int updateByPrimaryKey(Login record) {
		int i = iLoginDao.updateByPrimaryKey(record);
		return i;
	}
	
	@Test
	public void insert() {
		Login login = selectByUsername("username");
		System.out.println(login);
	}
}

