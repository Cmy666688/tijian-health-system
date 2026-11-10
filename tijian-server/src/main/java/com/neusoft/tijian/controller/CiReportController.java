package com.neusoft.tijian.controller;
import com.neusoft.tijian.po.CiReport;
import com.neusoft.tijian.service.ReportService;
import com.neusoft.tijian.OrderAccess;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/ciReport")
public class CiReportController {
    private final ReportService service;
    private final OrderAccess access;
    public CiReportController(ReportService service, OrderAccess access) { this.service=service; this.access=access; }
    @PostMapping("/listCiReport")
    public List<CiReport> list(@RequestBody CiReport request, HttpServletRequest servletRequest) {
        if (request.getOrderId()==null || request.getOrderId()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"预约编号须为正整数");
        access.requireOrder(servletRequest,request.getOrderId(),true);
        return service.listCiReports(request.getOrderId());
    }
}
