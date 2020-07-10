package com.chat.base;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
public class BaseDao {
	
	private SqlSessionFactory factory;
	
	private SqlSession session;
	
	public <T> T getMapper(Class<T> clz) {
		T models = null;
		if(factory==null) {	
			InputStream is = null;
			try {
				is = Resources.getResourceAsStream("spring-mybatis.xml");
				factory = new SqlSessionFactoryBuilder().build(is);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(session==null) {
				session = factory.openSession();
				models = session.getMapper(clz);
			}
		}
		return models;
	}
	
}
