package com.neusoft.tijiancms.po;
import java.util.List;
public class CiReport {
    private Integer cirId;
    private Integer ciId;
    private String ciName;
    private Integer orderId;
    private List<CiDetailedReport> details;
    public Integer getCirId() { return cirId; }
    public void setCirId(Integer value) { this.cirId = value; }
    public Integer getCiId() { return ciId; }
    public void setCiId(Integer value) { this.ciId = value; }
    public String getCiName() { return ciName; }
    public void setCiName(String value) { this.ciName = value; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer value) { this.orderId = value; }
    public List<CiDetailedReport> getDetails() { return details; }
    public void setDetails(List<CiDetailedReport> value) { this.details = value; }
}
