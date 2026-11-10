package com.neusoft.tijian.po;
import java.util.List;
public class CheckItem {
    private Integer ciId;
    private String ciName;
    private String ciContent;
    private String meaning;
    private List<CheckItemDetail> details;
    public Integer getCiId() { return ciId; }
    public void setCiId(Integer value) { this.ciId = value; }
    public String getCiName() { return ciName; }
    public void setCiName(String value) { this.ciName = value; }
    public String getCiContent() { return ciContent; }
    public void setCiContent(String value) { this.ciContent = value; }
    public String getMeaning() { return meaning; }
    public void setMeaning(String value) { this.meaning = value; }
    public List<CheckItemDetail> getDetails() { return details; }
    public void setDetails(List<CheckItemDetail> value) { this.details = value; }
}
