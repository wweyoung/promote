package com.kx.promote.bean;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {
    @Override
	public String toString() {
		return "Shop [id=" + id + ", name=" + name + ", bossid=" + bossid + ", state=" + state + ", boss=" + boss + "]";
	}

	public Shop(String name) {
		super();
		this.name = name;
	}

	public Shop() {
		super();
	}

	private Integer id;

    private String name;

    private Integer bossid;
    
    private String issuer;

    private Byte state;

    private Boss boss;

    private List<ShopPrice> priceList;
    
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

    public Boss getBoss() {
		return boss;
	}

	public void setBoss(Boss boss) {
		this.boss = boss;
		this.bossid = boss==null?null:boss.getId();
	}

	public Integer getBossid() {
        return bossid;
    }

    public void setBossid(Integer bossid) {
        this.bossid = bossid;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public List<ShopPrice> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ShopPrice> priceList) {
		this.priceList = priceList;
	}
}