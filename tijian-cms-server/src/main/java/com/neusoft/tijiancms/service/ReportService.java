package com.neusoft.tijiancms.service;
import com.neusoft.tijiancms.mapper.ReportMapper;
import com.neusoft.tijiancms.po.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service
public class ReportService {
    private final ReportMapper mapper;
    public ReportService(ReportMapper mapper) { this.mapper=mapper; }
    public List<OverallResult> overall(Integer orderId) { return mapper.listOverallResults(orderId); }
    public List<CiReport> reports(Integer orderId) { return mapper.listCiReports(orderId); }
    private void requireEditable(Integer orderId) {
        Integer state=mapper.lockOrderState(orderId);
        if(state==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"预约不存在");
        if(state!=1) throw new ResponseStatusException(HttpStatus.CONFLICT,"已归档报告不可编辑");
    }
    @Transactional
    public int create(Integer orderId) {
        requireEditable(orderId);
        if (mapper.countReports(orderId)>0) throw new ResponseStatusException(HttpStatus.CONFLICT,"报告模板已存在");
        int count=mapper.createItems(orderId);
        if(count==0) throw new ResponseStatusException(HttpStatus.CONFLICT,"套餐未配置检查项目");
        mapper.createDetails(orderId); return count;
    }
    @Transactional
    public int saveOverall(OverallResult value) {
        requireEditable(value.getOrderId());
        return mapper.insertOverall(value);
    }
    @Transactional
    public int updateOverall(OverallResult value) { requireEditable(value.getOrderId()); return mapper.updateOverall(value); }
    @Transactional
    public int deleteOverall(OverallResult value) { requireEditable(value.getOrderId()); return mapper.deleteOverall(value); }
    @Transactional
    public int updateDetails(List<CiDetailedReport> details) {
        int count=0;
        Integer orderId=details.get(0).getOrderId();
        requireEditable(orderId);
        for (CiDetailedReport d:details) {
            if(!orderId.equals(d.getOrderId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"只能同时更新同一预约的明细");
            int updated=mapper.updateDetail(d);
            if(updated!=1) throw new ResponseStatusException(HttpStatus.CONFLICT,"明细不存在或不属于指定报告");
            count+=updated;
        }
        return count;
    }
}
