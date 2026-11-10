package com.neusoft.tijian.mapper;
import com.neusoft.tijian.po.CiReport;
import com.neusoft.tijian.po.OverallResult;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ReportMapper {
    List<OverallResult> listOverallResults(@Param("orderId") Integer orderId);
    List<CiReport> listCiReports(@Param("orderId") Integer orderId);
}
