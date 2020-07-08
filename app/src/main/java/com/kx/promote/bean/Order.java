package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Order implements Serializable {
	//任务类

    @Override
	public String toString() {
		return "Order [id=" + id + ", groupid=" + groupid + ", needid=" + needid + ", state=" + state + ", price="
				+ price + ", no=" + no + "]";
	}

	private Integer id;

    private Integer groupid;//所属任务组id

    private Integer needid;//商家需求id

    private Byte state;//任务状态

    private BigDecimal price;//实际付款

    private String no;//淘宝订单编号

    private BigDecimal shopFee;//商家佣金
    
    private BigDecimal userFee;//业务员佣金
    
    private Group group;//任务组
    
    private Need need;//商家需求
    
    private List<String> imagelist;
    
	public final static byte PREPARE = 0;//未开始（管理员还没有正式开始任务）
	public final static byte DOING = 1;//进行中（任务进行中）
	public final static byte FILLIN = 2;//待补充（提交了任务但是填写的信息不完整）
	public final static byte FINISHED = 3;//已完成
	public final static byte UNDO = 4;//未做（由于搜索不到关键词等原因放弃了任务）
	public final static byte CANCELED = 10;//撤回（管理员撤回了任务）
	
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

	@JSONField(serialize=false)
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