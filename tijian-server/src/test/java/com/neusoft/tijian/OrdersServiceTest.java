package com.neusoft.tijian;

import com.neusoft.tijian.mapper.HospitalMapper;
import com.neusoft.tijian.mapper.OrdersMapper;
import com.neusoft.tijian.mapper.ScheduleMapper;
import com.neusoft.tijian.mapper.SetmealMapper;
import com.neusoft.tijian.mapper.UsersMapper;
import com.neusoft.tijian.po.Hospital;
import com.neusoft.tijian.po.Orders;
import com.neusoft.tijian.po.Setmeal;
import com.neusoft.tijian.po.Users;
import com.neusoft.tijian.service.OrdersService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrdersServiceTest {
    @Test void fullDayIsRejectedAfterHospitalLock() {
        OrdersMapper orders=mock(OrdersMapper.class); HospitalMapper hospitals=mock(HospitalMapper.class);
        SetmealMapper meals=mock(SetmealMapper.class); UsersMapper users=mock(UsersMapper.class); ScheduleMapper schedules=mock(ScheduleMapper.class);
        Orders order=new Orders(); order.setUserId("19000000000"); order.setHpId(1); order.setSmId(1); order.setOrderDate(LocalDate.now().plusDays(1));
        when(orders.lockUser(order.getUserId())).thenReturn(order.getUserId()); when(orders.lockHospital(1)).thenReturn(1);
        Hospital hospital=new Hospital(); hospital.setState(1); when(hospitals.findById(1)).thenReturn(hospital);
        Users profile=new Users(); profile.setRealName("测试"); profile.setSex(1); profile.setIdentityCard("110101200001010011"); profile.setBirthday(LocalDate.of(2000,1,1));
        when(users.findProfile(order.getUserId())).thenReturn(profile); when(meals.findById(1)).thenReturn(new Setmeal());
        when(schedules.capacity(eq(1),anyInt())).thenReturn(2); when(schedules.countDay(1,order.getOrderDate())).thenReturn(2);
        OrdersService service=new OrdersService(orders,hospitals,meals,users,schedules);
        ResponseStatusException error=assertThrows(ResponseStatusException.class,()->service.create(order));
        assertEquals(HttpStatus.CONFLICT,error.getStatus()); verify(orders,never()).insert(any());
        InOrder sequence=inOrder(orders); sequence.verify(orders).lockUser(order.getUserId()); sequence.verify(orders).lockHospital(1); sequence.verify(orders).countOpenByPatient(eq(order.getUserId()),anyString());
    }
    @Test void familyAppointmentUsesSuppliedPatientAndRejectsInvalidId() {
        OrdersMapper orders=mock(OrdersMapper.class); HospitalMapper hospitals=mock(HospitalMapper.class);
        SetmealMapper meals=mock(SetmealMapper.class); UsersMapper users=mock(UsersMapper.class); ScheduleMapper schedules=mock(ScheduleMapper.class);
        Orders order=new Orders(); order.setUserId("19000000000"); order.setHpId(1); order.setSmId(1);
        order.setOrderDate(LocalDate.now().plusDays(1)); order.setPatientType(2); order.setPatientName("  家属测试  ");
        order.setPatientSex(0); order.setPatientBirthday(LocalDate.of(1980,1,1)); order.setPatientIdentityCard("invalid");
        when(orders.lockUser(order.getUserId())).thenReturn(order.getUserId()); when(orders.lockHospital(1)).thenReturn(1);
        Hospital hospital=new Hospital(); hospital.setState(1); when(hospitals.findById(1)).thenReturn(hospital);
        when(meals.findById(1)).thenReturn(new Setmeal()); when(schedules.capacity(eq(1),anyInt())).thenReturn(20);
        when(orders.insert(order)).thenReturn(1);
        OrdersService service=new OrdersService(orders,hospitals,meals,users,schedules);
        assertEquals(HttpStatus.BAD_REQUEST,assertThrows(ResponseStatusException.class,()->service.create(order)).getStatus());
        verify(orders,never()).insert(any());
        order.setPatientIdentityCard("110101198001010022");
        assertEquals(1,service.create(order)); assertEquals("家属测试",order.getPatientName());
        verify(users,never()).findProfile(anyString());
    }
}
