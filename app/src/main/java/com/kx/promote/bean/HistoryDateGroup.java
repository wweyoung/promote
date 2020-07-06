package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HistoryDateGroup implements Serializable {
    private Date date;
    private Integer orderNumber;
    private Integer groupNumber;
    private Integer finishedOrderNumber;

    private BigDecimal actPrice;
    private List<Group> groupList;
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Integer getFinishedOrderNumber() {
        return finishedOrderNumber;
    }

    public void setFinishedOrderNumber(Integer finishedOrderNumber) {
        this.finishedOrderNumber = finishedOrderNumber;
    }

    public BigDecimal getActPrice() {
        return actPrice;
    }

    public void setActPrice(BigDecimal actPrice) {
        this.actPrice = actPrice;
    }
    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
