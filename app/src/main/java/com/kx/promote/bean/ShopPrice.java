package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ShopPrice implements Serializable {
    //店铺佣金规则类

    private Integer id;//规则id

    private Integer shopid;//商家id

    private BigDecimal max;

    private BigDecimal price;

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

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}