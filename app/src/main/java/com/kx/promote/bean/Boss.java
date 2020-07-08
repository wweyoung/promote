package com.kx.promote.bean;

import java.io.Serializable;

public class Boss implements Serializable {
    //老板信息类
    @Override
	public String toString() {
		return "Boss [id=" + id + ", name=" + name + ", phone=" + phone + ", state=" + state + "]";
	}

	private Integer id;

    private String name;

    private String phone;

    private Byte state;//状态

	public static byte ACTIVITY = 0;//正常
	public static byte DELETED = 1;//已被删除

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}