package com.chat.service.impl;

import java.util.List;

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

	public List<Record> selectByTargetIdOrSendId(String id) {
		//return iRecordDao.selectByTargetIdOrSendId(id);
		return null;
	}

}
