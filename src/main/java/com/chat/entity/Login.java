package com.chat.entity;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Login extends Record {
	public Login() {
		
	}
	private Login(String s) {
		
	}
	protected Login(String s,String d) {
		
	}
	
    private String id;

    private String tUsername;

    private String tPassword;

    private String tIp;

    private String tEmail;

    private String tAddr;

    private Date tTime;

    private String tImage;
    
    private String tNickName;

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

    public String gettPassword() {
        return tPassword;
    }

    public void settPassword(String tPassword) {
        this.tPassword = tPassword == null ? null : tPassword.trim();
    }

    public String gettIp() {
        return tIp;
    }

    public void settIp(String tIp) {
        this.tIp = tIp == null ? null : tIp.trim();
    }

    public String gettEmail() {
        return tEmail;
    }

    public void settEmail(String tEmail) {
        this.tEmail = tEmail == null ? null : tEmail.trim();
    }

    public String gettAddr() {
        return tAddr;
    }

    public void settAddr(String tAddr) {
        this.tAddr = tAddr == null ? null : tAddr.trim();
    }

    public Date gettTime() {
        return tTime;
    }

    public void settTime(Date tTime) {
        this.tTime = tTime;
    }

    public String gettImage() {
        return tImage;
    }

    public void settImage(String tImage) {
        this.tImage = tImage == null ? null : tImage.trim();
    }

	public String gettNickName() {
		return tNickName;
	}

	public void settNickName(String tNickName) {
		this.tNickName = tNickName;
	}
	
	private String getprivatemethod() {
		return "";
	}
    
    
}