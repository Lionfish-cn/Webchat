package com.chat.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.dao.RecordMapper;
import com.chat.entity.Record;
import com.chat.service.IRecordService;

@Service
public class RecordService implements IRecordService {

	@Autowired
	private RecordMapper iRecordDao;
	
	public int insert(Record record) {
		return iRecordDao.insert(record);
	}

	public Record selectByPrimaryKey(String id) {
		Record record = iRecordDao.selectByPrimaryKey(id);
		return record;
	}

	@Override
	public List<Record> selectPage(Map<String, Object> map) {
		return iRecordDao.selectPage(map);
	}

	@Override
	public List<Record> searchChatPerson(String username) {
		return iRecordDao.searchChatPerson(username);
	}

	@Override
	public int searchIsntRead(String username) {
		return iRecordDao.searchIsntRead(username);
	}

	@Override
	public int updateByPrimaryKeySelective(Record record) {
		return iRecordDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateIsntRead(Record record) {
		record.settIsRead(true);
		return updateByPrimaryKeySelective(record);
	}

}
