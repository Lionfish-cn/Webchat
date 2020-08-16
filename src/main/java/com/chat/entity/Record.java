package com.chat.entity;

import java.util.Date;

public class Record {
    private String id;

    private String tUsername;

    private String tRecord;

    private Date tTime;

    private String tTarget;

    private String tSend;
    
    private Boolean tIsRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String gettUsername() {
        return tUsername;
    }

    public void settUsername(String tUsername) {
        this.tUsername = tUsername == null ? null : tUsername.trim();
    }

    public String gettRecord() {
        return tRecord;
    }

    public void settRecord(String tRecord) {
        this.tRecord = tRecord == null ? null : tRecord.trim();
    }

    public Date gettTime() {
        return tTime;
    }

    public void settTime(Date tTime) {
        this.tTime = tTime;
    }

    public String gettTarget() {
        return tTarget;
    }

    public void settTarget(String tTarget) {
        this.tTarget = tTarget == null ? null : tTarget.trim();
    }

    public String gettSend() {
        return tSend;
    }

    public void settSend(String tSend) {
        this.tSend = tSend == null ? null : tSend.trim();
    }

	public Boolean gettIsRead() {
		return tIsRead;
	}

	public void settIsRead(Boolean tIsRead) {
		this.tIsRead = tIsRead;
	}
    
    
}