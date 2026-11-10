package com.neusoft.tijiancms.mapper;
import com.neusoft.tijiancms.po.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DoctorMapper {
    Doctor findProfile(@Param("docCode") String docCode);
    Doctor findCredentials(@Param("docCode") String docCode);
    int upgradePassword(@Param("docCode") String docCode, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
}
