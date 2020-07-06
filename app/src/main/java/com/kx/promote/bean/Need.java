package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Need implements Serializable {
    private Integer id;

    private Integer shopid;

    private String keyword;

    private Integer imageid;

    private BigDecimal price;

    private Integer allnumber;
    
    private Integer nownumber;

    private Integer finishednumber;

	private String note;

	private Byte priority;
	
    private Byte state;

    private Date time;
    
    private Shop shop;
    
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