package com.kx.promote.bean;

import java.util.ArrayList;
import java.util.List;

public class ShopNeed {
	private String shopname;
	private String imageurl;
	private List<Need> need;
	public ShopNeed(String shopname, String imageurl, List<Need> need) {
		super();
		this.shopname = shopname;
		this.imageurl = imageurl;
		this.need = need;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public ShopNeed(String shopname, String imageurl) {
		super();
		this.shopname = shopname;
		this.imageurl = imageurl;
		this.need = new ArrayList<Need>();
	}
	public ShopNeed() {
		this.need = new ArrayList<Need>();
	}
	@Override
	public String toString() {
		return "ShopNeed [shopname=" + shopname + ", imageurl=" + imageurl + ", need=" + need + "]";
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public List<Need> getNeed() {
		return need;
	}
	public void setNeed(List<Need> need) {
		this.need = need;
	}
	
}
