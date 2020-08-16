package com.chat.dao;

import java.util.List;
import java.util.Map;

import com.chat.entity.Record;

public interface RecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(String id);
    
    List<Record> selectPage(Map<String,Object> map);
    
    List<Record> searchChatPerson(String username);
    
    int searchIsntRead(String username);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);
    
}