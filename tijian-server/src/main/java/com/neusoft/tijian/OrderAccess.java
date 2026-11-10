package com.neusoft.tijian;
import com.neusoft.tijian.po.Orders;
import com.neusoft.tijian.service.OrdersService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
@Component
public class OrderAccess {
    private final OrdersService orders;
    public OrderAccess(OrdersService orders) { this.orders=orders; }
    public void requireUser(HttpServletRequest request, String userId) {
        String current=(String)request.getSession(false).getAttribute("userId");
        if(!current.equals(userId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"无权访问其他用户的数据");
    }
    public Orders requireOrder(HttpServletRequest request, Integer orderId, boolean archivedOnly) {
        Orders order=orders.find(orderId);
        if(order==null || !order.getUserId().equals(request.getSession(false).getAttribute("userId")) || (archivedOnly && order.getState()!=2))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"预约不存在或不可访问");
        return order;
    }
}
