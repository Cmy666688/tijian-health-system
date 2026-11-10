package com.neusoft.tijiancms.service;
import com.neusoft.tijiancms.dto.OrdersPageRequestDto;
import com.neusoft.tijiancms.dto.OrdersPageResponseDto;
import com.neusoft.tijiancms.mapper.OrdersMapper;
import com.neusoft.tijiancms.mapper.ReportMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.neusoft.tijiancms.po.Orders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class OrdersService {
    private final OrdersMapper mapper;
    private final ReportMapper reports;
    public OrdersService(OrdersMapper mapper, ReportMapper reports) { this.mapper=mapper; this.reports=reports; }
    @Transactional(readOnly=true)
    public OrdersPageResponseDto list(OrdersPageRequestDto filter) {
        OrdersPageResponseDto result=new OrdersPageResponseDto();
        result.setTotal(mapper.count(filter)); result.setPageNum(filter.getPageNum()); result.setPageSize(filter.getPageSize());
        result.setRecords(mapper.list(filter, ((long)filter.getPageNum()-1)*filter.getPageSize(), filter.getPageSize()));
        return result;
    }
    public Orders find(Integer orderId) { return mapper.findById(orderId); }
    @Transactional
    public int updateState(Integer orderId,Integer state) {
        Integer current=reports.lockOrderState(orderId);
        if(current==null || current!=1) return 0;
        if(reports.countReports(orderId)==0 || reports.countDetails(orderId)==0 || reports.countMissingDetails(orderId)>0 || reports.countOverall(orderId)==0)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"请先填写并保存所有检查结果及至少一条总检结论");
        return mapper.updateState(orderId,state);
    }
}
