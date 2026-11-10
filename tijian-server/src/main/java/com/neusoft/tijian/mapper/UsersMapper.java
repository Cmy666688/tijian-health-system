package com.neusoft.tijian.mapper;
import com.neusoft.tijian.po.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UsersMapper {
    Users findById(@Param("userId") String userId);
    Users findProfile(@Param("userId") String userId);
    Users findCredentials(@Param("userId") String userId);
    int upgradePassword(@Param("userId") String userId, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
    int insert(Users user);
}
