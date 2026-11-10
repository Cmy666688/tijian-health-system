package com.neusoft.tijian.service;
import com.neusoft.tijian.mapper.OrdersMapper;
import com.neusoft.tijian.mapper.HospitalMapper;
import com.neusoft.tijian.mapper.SetmealMapper;
import com.neusoft.tijian.mapper.UsersMapper;
import com.neusoft.tijian.mapper.ScheduleMapper;
import com.neusoft.tijian.po.Users;
import com.neusoft.tijian.po.Orders;
import com.neusoft.tijian.po.Hospital;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service
public class OrdersService {
    private final OrdersMapper mapper;
    private final HospitalMapper hospitals;
    private final SetmealMapper meals;
    private final UsersMapper users;
    private final ScheduleMapper schedules;
    public OrdersService(OrdersMapper mapper, HospitalMapper hospitals, SetmealMapper meals, UsersMapper users, ScheduleMapper schedules) {
        this.mapper=mapper; this.hospitals=hospitals; this.meals=meals; this.users=users; this.schedules=schedules;
    }
    public int countOpen(String userId) { return mapper.countOpen(userId); }
    public List<Orders> list(String userId, Integer state) { return mapper.list(userId,state); }
    public Orders find(Integer orderId) { return mapper.findById(orderId); }
    @Transactional
    public int create(Orders order) {
        if (mapper.lockUser(order.getUserId()) == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用户不存在");
        // 同一医院的预约串行化；首次一致性读取在获取医院锁后，避免并发超额。
        if (mapper.lockHospital(order.getHpId()) == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "医院不存在或未开放");
        if (mapper.countOpen(order.getUserId()) > 0) throw new ResponseStatusException(HttpStatus.CONFLICT, "已有未归档预约");
        if (order.getPatientType()==null) order.setPatientType(1);
        if (order.getPatientType()==1) {
            Users profile=users.findProfile(order.getUserId());
            order.setPatientName(profile.getRealName()); order.setPatientSex(profile.getSex());
            order.setPatientIdentityCard(profile.getIdentityCard()); order.setPatientBirthday(profile.getBirthday());
        } else if (order.getPatientType()==2) {
            if(order.getPatientName()==null || order.getPatientName().trim().isEmpty() || order.getPatientName().trim().length()>20 ||
                order.getPatientSex()==null || (order.getPatientSex()!=0 && order.getPatientSex()!=1) ||
                order.getPatientIdentityCard()==null || !order.getPatientIdentityCard().matches("[0-9]{17}[0-9Xx]") ||
                order.getPatientBirthday()==null || order.getPatientBirthday().isBefore(LocalDate.of(1900,1,1)) || order.getPatientBirthday().isAfter(LocalDate.now()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"家属体检人信息不完整或格式不正确");
            order.setPatientName(order.getPatientName().trim());
            order.setPatientIdentityCard(order.getPatientIdentityCard().toUpperCase(java.util.Locale.ROOT));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"体检人类型无效");
        Hospital hospital = hospitals.findById(order.getHpId());
        if (hospital == null || hospital.getState() != 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "医院不存在或未开放");
        if (meals.findById(order.getSmId()) == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "套餐不存在");
        if (order.getOrderDate().isBefore(LocalDate.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "预约日期不能早于今天");
        if (order.getOrderDate().isAfter(LocalDate.of(2100,12,31))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "预约日期超出支持范围");
        Integer capacity=schedules.capacity(order.getHpId(),order.getOrderDate().getDayOfWeek().getValue());
        if(capacity==null || capacity<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "所选日期未开放预约");
        if(schedules.countDay(order.getHpId(),order.getOrderDate())>=capacity) throw new ResponseStatusException(HttpStatus.CONFLICT, "所选日期预约名额已满");
        return mapper.insert(order);
    }
    @Transactional
    public int remove(Integer id, String userId) {
        try { return mapper.deleteOpen(id,userId); }
        catch (DataIntegrityViolationException e) { throw new ResponseStatusException(HttpStatus.CONFLICT, "预约已有关联报告，无法删除"); }
    }
}
