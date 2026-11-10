package com.neusoft.tijian.po;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
public class Users {
    private String userId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String realName;
    private Integer sex;
    private String identityCard;
    private LocalDate birthday;
    private Integer userType;
    public String getUserId() { return userId; }
    public void setUserId(String v) { userId = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { password = v; }
    public String getRealName() { return realName; }
    public void setRealName(String v) { realName = v; }
    public Integer getSex() { return sex; }
    public void setSex(Integer v) { sex = v; }
    public String getIdentityCard() { return identityCard; }
    public void setIdentityCard(String v) { identityCard = v; }
    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate v) { birthday = v; }
    public Integer getUserType() { return userType; }
    public void setUserType(Integer v) { userType = v; }
}
