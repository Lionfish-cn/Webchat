package com.chat.service;

import java.util.List;

import com.chat.entity.Record;

public interface IRecordService {
	int insert(Record record);

	Record selectByPrimaryKey(String id);
	
	List<Record> selectByTargetIdOrSendId(String id);
}
