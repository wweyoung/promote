package com.kx.promote.bean;

import java.util.Date;

public class File {
    private Integer id;

    private Byte type;

    private String url;

    private Integer referid;
    
    private Date time;

    private Byte position;
	public final static byte USER_IMAGE = 0;
	public final static byte GOODS_IMAGE = 1;
	public final static byte ORDER_IMAGE = 2;
	public final static byte GROUP_IMAGE = 3;
	public final static int NULL_REFER = 0;
	public final static byte LOCAL = 0;
	public final static byte QINIU = 1;
	public final static byte NOT_EXIST = 10;

    
    public File() {
		super();
	}

	public File(String url, Byte type,  Integer referid,Byte position) {
		super();
		this.type = type;
		this.url = url;
		this.referid = referid;
		this.setPosition(position);
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public static boolean typeIsImage(byte type) {
    	return type==File.GOODS_IMAGE || type==File.GROUP_IMAGE || type==File.ORDER_IMAGE
    			|| type==File.USER_IMAGE;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getReferid() {
        return referid;
    }

    public void setReferid(Integer referid) {
        this.referid = referid;
    }

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Byte getPosition() {
		return position;
	}

	public void setPosition(Byte position) {
		this.position = position;
	}
}