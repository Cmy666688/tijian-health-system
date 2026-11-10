package com.neusoft.tijian.controller;
import com.neusoft.tijian.dto.CalendarRequestDto;
import com.neusoft.tijian.dto.CalendarResponseDto;
import com.neusoft.tijian.po.Hospital;
import com.neusoft.tijian.service.HospitalService;
import com.neusoft.tijian.mapper.ScheduleMapper;
import com.neusoft.tijian.po.HospitalSchedule;
import com.neusoft.tijian.po.DailyBooked;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/calendar")
public class CalendarController {
    private final HospitalService hospitals;
    private final ScheduleMapper schedules;
    public CalendarController(HospitalService hospitals, ScheduleMapper schedules) { this.hospitals=hospitals; this.schedules=schedules; }
    @PostMapping("/listAppointmentCalendar")
    public List<CalendarResponseDto> list(@RequestBody CalendarRequestDto request) {
        if (request.getHpId()==null || request.getHpId()<=0 || request.getYear()==null || request.getYear()<1900 || request.getYear()>2100 || request.getMonth()==null || request.getMonth()<1 || request.getMonth()>12)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"医院编号、年份或月份无效");
        Hospital h=hospitals.findById(request.getHpId());
        if (h==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"医院不存在");
        YearMonth month=YearMonth.of(request.getYear(),request.getMonth());
        Map<Integer,Integer> capacityByWeekday=new HashMap<>();
        for(HospitalSchedule schedule:schedules.list(request.getHpId())) capacityByWeekday.put(schedule.getWeekday(),schedule.getDailyCapacity());
        Map<LocalDate,Integer> bookedByDate=new HashMap<>();
        for(DailyBooked booked:schedules.countMonth(request.getHpId(),month.atDay(1),month.plusMonths(1).atDay(1))) bookedByDate.put(booked.getOrderDate(),booked.getBookedCount());
        List<CalendarResponseDto> result=new ArrayList<>();
        for (int i=1;i<=month.lengthOfMonth();i++) {
            LocalDate date=month.atDay(i);
            CalendarResponseDto day=new CalendarResponseDto();
            day.setDate(date); day.setDayOfWeek(date.getDayOfWeek().getValue());
            int capacity=capacityByWeekday.getOrDefault(date.getDayOfWeek().getValue(),0);
            int remaining=Math.max(0,capacity-bookedByDate.getOrDefault(date,0));
            day.setCapacity(capacity); day.setRemaining(remaining);
            day.setAvailable(h.getState()==1 && !date.isBefore(LocalDate.now()) && remaining>0);
            result.add(day);
        }
        return result;
    }
}
