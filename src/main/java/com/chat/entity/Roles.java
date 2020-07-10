package com.chat.entity;

import java.util.Date;

import com.chat.util.StringUtil;
import com.chat.util.generate.IDGenerator;

public class Roles {
    private String id;

    private String tRole;

    private String tRoleUrl;

    private String tUserId;

    private Date tTime;

    private String tSuperUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
    	if(StringUtil.isNull(id))
    		id = IDGenerator.generatorID();
        this.id = id == null ? null : id.trim();
    }

    public String gettRole() {
        return tRole;
    }

    public void settRole(String tRole) {
        this.tRole = tRole == null ? null : tRole.trim();
    }

    public String gettRoleUrl() {
        return tRoleUrl;
    }

    public void settRoleUrl(String tRoleUrl) {
        this.tRoleUrl = tRoleUrl == null ? null : tRoleUrl.trim();
    }

    public String gettUserId() {
        return tUserId;
    }

    public void settUserId(String tUserId) {
        this.tUserId = tUserId == null ? null : tUserId.trim();
    }

    public Date gettTime() {
        return tTime;
    }

    public void settTime(Date tTime) {
        this.tTime = tTime;
    }

    public String gettSuperUser() {
        return tSuperUser;
    }

    public void settSuperUser(String tSuperUser) {
        this.tSuperUser = tSuperUser == null ? null : tSuperUser.trim();
    }
}