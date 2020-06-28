package com.kx.promote.bean;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class User {
    @Override
	public String toString() {
		return "User [id=" + id + ", user=" + user + ", name=" + name + ", phone=" + phone + ", email=" + email
				+ ", state=" + state + ", lastnumber=" + lastnumber + ", logincode=" + logincode + ", fee=" + fee + "]";
	}

	private Integer id;

    private String user;
    
    @JSONField(serialize=false)  
    private String password;

    private String name;

    private String phone;

    private String email;

    private Byte state;

    private Integer lastnumber;

    private Byte identity;
    
    private String note;
    
    private List<List<Integer>> allotNeedList;
    @JSONField(serialize=false)  
    private String logincode;

    private BigDecimal fee;

	public final static byte ACTIVITY = 0;//正常
	public final static byte VACATION = 1;//休假
	public final static byte DELETED = 2;//删除
	public final static byte WORKER = 0;//业务员
	public final static byte ACCOUNTANT = 1;//会计员
	public final static byte ADMINISTRATOR = 2;//管理员
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user == null ? null : user.trim();
    }

    @JSONField(serialize=false)  
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Integer getLastnumber() {
        return lastnumber;
    }

    public void setLastnumber(Integer lastnumber) {
        this.lastnumber = lastnumber;
    }

    public Byte getIdentity() {
        return identity;
    }

    public void setIdentity(Byte identity) {
        this.identity = identity;
    }

    @JSONField(serialize=false)  
    public String getLogincode() {
        return logincode;
    }

    public void setLogincode(String logincode) {
        this.logincode = logincode == null ? null : logincode.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

	public List<List<Integer>> getAllotNeedList() {
		return allotNeedList;
	}

	public void setAllotNeedList(List<List<Integer>> allotNeedList) {
		this.allotNeedList = allotNeedList;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}