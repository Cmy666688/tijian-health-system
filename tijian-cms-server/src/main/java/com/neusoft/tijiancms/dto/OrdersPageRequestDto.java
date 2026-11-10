package com.neusoft.tijiancms.dto;
import java.time.LocalDate;

public class OrdersPageRequestDto {
    private Integer pageNum;
    private Integer pageSize;
    private String userId;
    private Integer state;
    private Integer smId;
    private String realName;
    private Integer sex;
    private LocalDate orderDate;
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer value) { this.pageNum = value; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer value) { this.pageSize = value; }
    public String getUserId() { return userId; }
    public void setUserId(String value) { this.userId = value; }
    public Integer getState() { return state; }
    public void setState(Integer value) { this.state = value; }
    public Integer getSmId() { return smId; }
    public void setSmId(Integer value) { this.smId = value; }
    public String getRealName() { return realName; }
    public void setRealName(String value) { this.realName=value; }
    public Integer getSex() { return sex; }
    public void setSex(Integer value) { this.sex=value; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate value) { this.orderDate=value; }
}
