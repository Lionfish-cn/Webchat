package com.chat.service;

import java.util.List;
import java.util.Map;

import com.chat.entity.Record;

public interface IRecordService {
	int insert(Record record);

	Record selectByPrimaryKey(String id);
	
	List<Record> selectPage(Map<String,Object> map);
	
	List<String> searchChatPerson(String username);
}
