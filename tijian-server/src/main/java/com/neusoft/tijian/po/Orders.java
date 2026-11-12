package com.neusoft.tijian.po;
import java.time.LocalDate;
public class Orders {
    private String paymentStatus;
    private java.math.BigDecimal paymentAmount;
    private Integer orderId;
    private LocalDate orderDate;
    private String userId;
    private Integer hpId;
    private Integer smId;
    private Integer state;
    private Integer patientType;
    private String patientName;
    private Integer patientSex;
    private String patientIdentityCard;
    private LocalDate patientBirthday;
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String value) { paymentStatus=value; }
    public java.math.BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(java.math.BigDecimal value) { paymentAmount=value; }
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
    public Integer getPatientType() { return patientType; }
    public void setPatientType(Integer value) { this.patientType=value; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String value) { this.patientName=value; }
    public Integer getPatientSex() { return patientSex; }
    public void setPatientSex(Integer value) { this.patientSex=value; }
    public String getPatientIdentityCard() { return patientIdentityCard; }
    public void setPatientIdentityCard(String value) { this.patientIdentityCard=value; }
    public LocalDate getPatientBirthday() { return patientBirthday; }
    public void setPatientBirthday(LocalDate value) { this.patientBirthday=value; }
}
