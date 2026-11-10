package com.neusoft.tijian.controller;
import com.neusoft.tijian.po.Orders;
import com.neusoft.tijian.OrderAccess;
import javax.servlet.http.HttpServletRequest;
import com.neusoft.tijian.service.OrdersService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService service;
    private final OrderAccess access;
    public OrdersController(OrdersService service, OrderAccess access) { this.service=service; this.access=access; }
    private void user(Orders o) {
        if (o.getUserId()==null || !o.getUserId().matches("[0-9]{11}")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"用户编号须为11位手机号");
    }
    private void id(Orders o) {
        if (o.getOrderId()==null || o.getOrderId()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"预约编号须为正整数");
    }
    @PostMapping("/getOrdersByUserId") public int getOrdersByUserId(@RequestBody Orders o, HttpServletRequest request) { user(o); access.requireUser(request,o.getUserId()); return service.countOpen(o.getUserId()); }
    @PostMapping("/saveOrders") public int saveOrders(@RequestBody Orders o, HttpServletRequest request) {
        user(o); access.requireUser(request,o.getUserId());
        if (o.getHpId()==null || o.getHpId()<=0 || o.getSmId()==null || o.getSmId()<=0 || o.getOrderDate()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"医院、套餐和预约日期必填");
        return service.create(o);
    }
    @PostMapping("/listOrdersByUserId") public List<Orders> list(@RequestBody Orders o, HttpServletRequest request) {
        user(o); access.requireUser(request,o.getUserId());
        if (o.getState()==null || (o.getState()!=1 && o.getState()!=2)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"状态须为1或2");
        return service.list(o.getUserId(),o.getState());
    }
    @PostMapping("/getOrdersById") public Orders get(@RequestBody Orders o, HttpServletRequest request) { id(o); return access.requireOrder(request,o.getOrderId(),false); }
    @PostMapping("/removeOrders") public int remove(@RequestBody Orders o, HttpServletRequest request) { id(o); user(o); access.requireUser(request,o.getUserId()); access.requireOrder(request,o.getOrderId(),false); return service.remove(o.getOrderId(),o.getUserId()); }
}
