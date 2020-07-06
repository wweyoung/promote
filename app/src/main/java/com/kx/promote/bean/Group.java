package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Group implements Serializable {
    private Integer id;

    private Integer userid;

    private Byte state;

    private String customer;

    private String note;

    private Date time;
    
    private Date protecttime;

    private Date submittime;

    public Date getSubmittime() {
        return submittime;
    }

    public void setSubmittime(Date submittime) {
        this.submittime = submittime;
    }

    private User user;
    
    private List<Order> orderlist;
    
    private List<String> imagelist;
    
	public final static byte PREPARE = 0;
	public final static byte DOING = 1;
	public final static byte FILLIN = 2;
	public final static byte FINISHED = 3;
	public final static byte DELETED = 10;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Byte getState() {
        return state;
    }

    @JSONField(serialize=false)  
    public String getStateString() {
    	switch(state) {
    	case Group.PREPARE:return "未开始";
    	case Group.DOING:return "进行中";
    	case Group.FILLIN:return "待补充";
    	case Group.FINISHED:return "已完成";
    	case Group.DELETED:return "已撤回";
    	}
    	return "未知";
    }
    public void setState(Byte state) {
        this.state = state;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer == null ? null : customer.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.userid = user==null?null:user.getId();
	}

	public Date getProtecttime() {
		return protecttime;
	}

	public void setProtecttime(Date protecttime) {
		this.protecttime = protecttime;
	}

	public List<Order> getOrderlist() {
		return orderlist;
	}

	public void setOrderlist(List<Order> orderlist) {
		this.orderlist = orderlist;
	}

	public List<String> getImagelist() {
		return imagelist;
	}

	public void setImagelist(List<String> imagelist) {
		this.imagelist = imagelist;
	}
	public void setImagelistByFile(List<File> filelist) {
		List<String> imagelist = new ArrayList<String>();
		for(File file:filelist) {
			imagelist.add(file.getUrl());
		}
		this.setImagelist(imagelist);
	}
    @JSONField(serialize=false)
    public BigDecimal getPreprice(){
        BigDecimal preprice = new BigDecimal(0);
        if(this.getOrderlist()==null)
            return preprice;
        for(Order order:this.getOrderlist()){
            preprice = preprice.add(order.getNeed().getPrice());
        }
        return preprice;
    }
    @JSONField(serialize=false)
    public BigDecimal getActprice(){
        BigDecimal actprice = new BigDecimal(0);
        if(this.getOrderlist()==null)
            return actprice;
        for(Order order:this.getOrderlist()){
            if(order.getPrice()==null)
                continue;
            actprice = actprice.add(order.getPrice());
        }
        return actprice;
    }

}