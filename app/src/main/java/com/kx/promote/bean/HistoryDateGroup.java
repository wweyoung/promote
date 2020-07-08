package com.kx.promote.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HistoryDateGroup implements Serializable {
    //统计的某一天的任务数据类

    private Date date;//日期
    private Integer orderNumber;//任务总数
    private Integer groupNumber;//任务组总数
    private Integer finishedOrderNumber;//已完成任务数

    private BigDecimal actPrice;//实际付款
    private List<Group> groupList;//任务组列表
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
