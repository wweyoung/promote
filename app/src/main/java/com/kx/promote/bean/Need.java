package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Need implements Serializable {
    //淘宝商家需求

    private Integer id;

    private Integer shopid;//店铺id

    private String keyword;//关键词

    private Integer imageid;//商品主图文件id

    private BigDecimal price;//预付款

    private Integer allnumber;//一共要刷的单数
    
    private Integer nownumber;//已经分配给业务员的单数

    private Integer finishednumber;//业务员们做完的单数

	private String note;//需求备注信息

	private Byte priority;//需求优先级，越高越优先
	
    private Byte state;//状态

    private Date time;//时间
    
    private Shop shop;//店铺
    
    public final static byte ACTIVITY = 0;//正常
	public final static byte CANCELED = 1;//撤回

    public Integer getNownumber() {
		return nownumber;
	}

	public void setNownumber(Integer nownumber) {
		this.nownumber = nownumber;
	}

	public Integer getFinishednumber() {
		return finishednumber;
	}

	public void setFinishednumber(Integer finishednumber) {
		this.finishednumber = finishednumber;
	}
	@Override
	public String toString() {
		return "Need [id=" + id + ", shopid=" + shopid + ", keyword=" + keyword + ", imageid=" + imageid + ", price="
				+ price + ", allnumber=" + allnumber + ", note=" + note + ", state=" + state + ", time=" + time
				+ ", shop=" + shop + ", image=" + image + "]";
	}

	private File image;

    public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
		this.shopid = shop==null?null:shop.getId();
	}

    

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
		this.imageid = image==null?null:image.getId();
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Integer getImageid() {
        return imageid;
    }

    public void setImageid(Integer imageid) {
        this.imageid = imageid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAllnumber() {
        return allnumber;
    }

    public void setAllnumber(Integer allnumber) {
        this.allnumber = allnumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

	public Byte getPriority() {
		return priority;
	}

	public void setPriority(Byte priority) {
		this.priority = priority;
	}
}