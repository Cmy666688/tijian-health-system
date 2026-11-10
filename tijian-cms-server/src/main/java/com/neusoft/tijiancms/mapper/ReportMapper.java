package com.neusoft.tijiancms.mapper;
import com.neusoft.tijiancms.po.*;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ReportMapper {
    List<OverallResult> listOverallResults(@Param("orderId") Integer orderId);
    List<CiReport> listCiReports(@Param("orderId") Integer orderId);
    Integer lockOrderState(@Param("orderId") Integer orderId);
    int countReports(@Param("orderId") Integer orderId);
    int countDetails(@Param("orderId") Integer orderId);
    int countMissingDetails(@Param("orderId") Integer orderId);
    int countOverall(@Param("orderId") Integer orderId);
    int createItems(@Param("orderId") Integer orderId);
    int createDetails(@Param("orderId") Integer orderId);
    int insertOverall(OverallResult value);
    int updateOverall(OverallResult value);
    int deleteOverall(OverallResult value);
    int updateDetail(CiDetailedReport value);
}
