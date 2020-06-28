package com.kx.promote.bean;

public class Boss {
    @Override
	public String toString() {
		return "Boss [id=" + id + ", name=" + name + ", phone=" + phone + ", state=" + state + "]";
	}

	private Integer id;

    private String name;

    private String phone;

    private Byte state;

	public static byte ACTIVITY = 0;
	public static byte DELETED = 1;

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