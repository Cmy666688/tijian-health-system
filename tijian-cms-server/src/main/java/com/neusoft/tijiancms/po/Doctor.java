package com.neusoft.tijiancms.po;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Doctor {
    private Integer docId;
    private String docCode;
    private String realName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Integer sex;
    private Integer deptno;
    public Integer getDocId() { return docId; }
    public void setDocId(Integer value) { docId=value; }
    public String getDocCode() { return docCode; }
    public void setDocCode(String value) { docCode=value; }
    public String getRealName() { return realName; }
    public void setRealName(String value) { realName=value; }
    public String getPassword() { return password; }
    public void setPassword(String value) { password=value; }
    public Integer getSex() { return sex; }
    public void setSex(Integer value) { sex=value; }
    public Integer getDeptno() { return deptno; }
    public void setDeptno(Integer value) { deptno=value; }
}
