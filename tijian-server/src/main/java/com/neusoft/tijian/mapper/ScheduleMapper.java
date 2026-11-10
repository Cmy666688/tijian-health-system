package com.neusoft.tijian.mapper;
import com.neusoft.tijian.po.HospitalSchedule;
import com.neusoft.tijian.po.DailyBooked;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ScheduleMapper {
    List<HospitalSchedule> list(@Param("hpId") Integer hpId);
    Integer capacity(@Param("hpId") Integer hpId, @Param("weekday") Integer weekday);
    int countDay(@Param("hpId") Integer hpId, @Param("date") LocalDate date);
    List<DailyBooked> countMonth(@Param("hpId") Integer hpId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
