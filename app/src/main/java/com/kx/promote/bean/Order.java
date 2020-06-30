package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Order implements Serializable {
    @Override
	public String toString() {
		return "Order [id=" + id + ", groupid=" + groupid + ", needid=" + needid + ", state=" + state + ", price="
				+ price + ", no=" + no + "]";
	}

	private Integer id;

    private Integer groupid;

    private Integer needid;

    private Byte state;

    private BigDecimal price;

    private String no;

    private BigDecimal shopFee;
    
    private BigDecimal userFee;
    
    private Group group;
    
    private Need need;
    
    private List<String> imagelist;
    
	public final static byte PREPARE = 0;
	public final static byte DOING = 1;
	public final static byte FILLIN = 2;
	public final static byte FINISHED = 3;
	public final static byte UNDO = 4;
	public final static byte CANCELED = 10;
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getNeedid() {
        return needid;
    }

    public void setNeedid(Integer needid) {
        this.needid = needid;
    }

    public Byte MsgException() {
        return state;
    }

    public Byte getState() {
    	return this.state;
    }
    @JSONField(serialize=false)  
    public String getStateString() {
    	return Order.getStateString(state);
    }
    public void setState(Byte state) {
        this.state = state;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
		this.groupid = group==null?null:group.getId();
	}

	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
		this.needid = need==null?null:need.getId();
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
	public static String getStateString(Byte state) {
	   	switch(state) {
    	case Order.PREPARE:return "未开始";
    	case Order.DOING:return "进行中";
    	case Order.FILLIN:return "待补充";
    	case Order.FINISHED:return "已完成";
    	case Order.UNDO:return "未做";
    	}
    	return "未知";
	}

	public BigDecimal getShopFee() {
		return shopFee;
	}

	public void setShopFee(BigDecimal shopFee) {
		this.shopFee = shopFee;
	}

	public BigDecimal getUserFee() {
		return userFee;
	}

	public void setUserFee(BigDecimal userFee) {
		this.userFee = userFee;
	}

}