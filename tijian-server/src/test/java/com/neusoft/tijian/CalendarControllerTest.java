package com.neusoft.tijian;

import com.neusoft.tijian.controller.CalendarController;
import com.neusoft.tijian.dto.CalendarRequestDto;
import com.neusoft.tijian.dto.CalendarResponseDto;
import com.neusoft.tijian.mapper.ScheduleMapper;
import com.neusoft.tijian.po.DailyBooked;
import com.neusoft.tijian.po.Hospital;
import com.neusoft.tijian.po.HospitalSchedule;
import com.neusoft.tijian.service.HospitalService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CalendarControllerTest {
    @Test void calendarRespectsCapacityAndClosedDays() {
        HospitalService hospitals=mock(HospitalService.class);
        ScheduleMapper schedules=mock(ScheduleMapper.class);
        Hospital hospital=new Hospital(); hospital.setState(1);
        when(hospitals.findById(1)).thenReturn(hospital);
        HospitalSchedule monday=new HospitalSchedule(); monday.setWeekday(1); monday.setDailyCapacity(2);
        when(schedules.list(1)).thenReturn(Collections.singletonList(monday));
        LocalDate day=LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        DailyBooked booked=new DailyBooked(); booked.setOrderDate(day); booked.setBookedCount(2);
        when(schedules.countMonth(eq(1), any(LocalDate.class), any(LocalDate.class))).thenReturn(Arrays.asList(booked));
        CalendarRequestDto request=new CalendarRequestDto(); request.setHpId(1); request.setYear(day.getYear()); request.setMonth(day.getMonthValue());
        List<CalendarResponseDto> result=new CalendarController(hospitals,schedules).list(request);
        assertEquals(YearMonth.from(day).lengthOfMonth(),result.size());
        CalendarResponseDto full=result.get(day.getDayOfMonth()-1);
        assertEquals(2,full.getCapacity()); assertEquals(0,full.getRemaining()); assertFalse(full.getAvailable());
        LocalDate closed=day.plusDays(1);
        if(YearMonth.from(day).equals(YearMonth.from(closed))) {
            CalendarResponseDto noSchedule=result.get(closed.getDayOfMonth()-1);
            assertEquals(0,noSchedule.getCapacity()); assertFalse(noSchedule.getAvailable());
        }
    }
}
