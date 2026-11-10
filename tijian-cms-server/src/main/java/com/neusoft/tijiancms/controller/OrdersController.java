package com.neusoft.tijiancms.controller;
import com.neusoft.tijiancms.dto.OrdersPageRequestDto;
import com.neusoft.tijiancms.dto.OrdersPageResponseDto;
import com.neusoft.tijiancms.po.Orders;
import com.neusoft.tijiancms.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService service;
    public OrdersController(OrdersService service) { this.service=service; }
    private void validateId(Orders request) {
        if (request.getOrderId()==null || request.getOrderId()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"预约编号须为正整数");
    }
    @PostMapping("/listOrders") public OrdersPageResponseDto list(@RequestBody OrdersPageRequestDto request) {
        if (request.getPageNum()==null) request.setPageNum(1);
        if (request.getPageSize()==null) request.setPageSize(10);
        if (request.getPageNum()<1 || request.getPageSize()<1 || request.getPageSize()>100 ||
            (request.getState()!=null && request.getState()!=1 && request.getState()!=2) ||
            (request.getSmId()!=null && request.getSmId()<1) ||
            (request.getSex()!=null && request.getSex()!=0 && request.getSex()!=1) ||
            (request.getRealName()!=null && request.getRealName().length()>20) ||
            (request.getUserId()!=null && !request.getUserId().isEmpty() && !request.getUserId().matches("[0-9]{11}")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"分页或筛选参数无效");
        return service.list(request);
    }
    @PostMapping("/getOrdersById") public Orders get(@RequestBody Orders request) { validateId(request); return service.find(request.getOrderId()); }
    @PostMapping("/updateOrdersState") public int update(@RequestBody Orders request) {
        validateId(request);
        if (request.getState()==null || request.getState()!=2) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"仅支持归档（状态2）");
        return service.updateState(request.getOrderId(),request.getState());
    }
}
