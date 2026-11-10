package com.neusoft.tijian.mapper;
import com.neusoft.tijian.po.Hospital;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface HospitalMapper {
    List<Hospital> listByState(@Param("state") Integer state);
    Hospital findById(@Param("hpId") Integer hpId);
}
