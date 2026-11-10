package com.neusoft.tijiancms.po;
import java.time.LocalDate;
public class Orders {
    private Integer orderId;
    private LocalDate orderDate;
    private String userId;
    private Integer hpId;
    private Integer smId;
    private Integer state;
    private String realName;
    private String bookingName;
    private Integer patientType;
    private String patientIdentityCard;
    private LocalDate patientBirthday;
    private Integer sex;
    private String hospitalName;
    private String setmealName;
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer value) { this.orderId = value; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate value) { this.orderDate = value; }
    public String getUserId() { return userId; }
    public void setUserId(String value) { this.userId = value; }
    public Integer getHpId() { return hpId; }
    public void setHpId(Integer value) { this.hpId = value; }
    public Integer getSmId() { return smId; }
    public void setSmId(Integer value) { this.smId = value; }
    public Integer getState() { return state; }
    public void setState(Integer value) { this.state = value; }
    public String getRealName() { return realName; }
    public void setRealName(String value) { this.realName = value; }
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String value) { this.hospitalName = value; }
    public String getSetmealName() { return setmealName; }
    public void setSetmealName(String value) { this.setmealName = value; }
    public Integer getSex() { return sex; }
    public void setSex(Integer value) { this.sex=value; }
    public String getBookingName() { return bookingName; }
    public void setBookingName(String value) { this.bookingName=value; }
    public Integer getPatientType() { return patientType; }
    public void setPatientType(Integer value) { this.patientType=value; }
    public String getPatientIdentityCard() { return patientIdentityCard; }
    public void setPatientIdentityCard(String value) { this.patientIdentityCard=value; }
    public LocalDate getPatientBirthday() { return patientBirthday; }
    public void setPatientBirthday(LocalDate value) { this.patientBirthday=value; }
}
