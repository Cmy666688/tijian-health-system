package com.neusoft.tijiancms.controller;
import com.neusoft.tijiancms.po.*;
import com.neusoft.tijiancms.service.ReportService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService service) { this.service=service; }
    private void order(Integer id) { if(id==null || id<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"预约编号须为正整数"); }
    private void overall(OverallResult o,boolean withId,boolean withText) {
        order(o.getOrderId());
        if(withId && (o.getOrId()==null || o.getOrId()<=0)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"结论编号无效");
        if(withText && (o.getTitle()==null || o.getTitle().trim().isEmpty() || o.getTitle().length()>40 || (o.getContent()!=null && o.getContent().length()>400)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"结论标题或内容无效");
    }
    @PostMapping("/overallResult/listOverallResultByOrderId") public List<OverallResult> overall(@RequestBody OverallResult o) { order(o.getOrderId()); return service.overall(o.getOrderId()); }
    @PostMapping("/overallResult/saveOverallResult") public int save(@RequestBody OverallResult o) { overall(o,false,true); return service.saveOverall(o); }
    @PostMapping("/overallResult/updateOverallResult") public int update(@RequestBody OverallResult o) { overall(o,true,true); return service.updateOverall(o); }
    @PostMapping("/overallResult/removeOverallResult") public int remove(@RequestBody OverallResult o) { overall(o,true,false); return service.deleteOverall(o); }
    @PostMapping("/ciReport/createReportTemplate") public int create(@RequestBody Orders o) { order(o.getOrderId()); return service.create(o.getOrderId()); }
    @PostMapping("/ciReport/listCiReport") public List<CiReport> list(@RequestBody CiReport o) { order(o.getOrderId()); return service.reports(o.getOrderId()); }
    @PostMapping("/ciDetailedReport/updateCiDetailedReport") public int updateDetails(@RequestBody List<CiDetailedReport> details) {
        if(details==null || details.isEmpty() || details.size()>500) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"明细数量无效");
        for(CiDetailedReport d:details) {
            if(d==null || d.getCidrId()==null || d.getCidrId()<=0 || d.getCiId()==null || d.getCiId()<=0 || d.getOrderId()==null || d.getOrderId()<=0 || d.getIsError()==null || (d.getIsError()!=0 && d.getIsError()!=1) || (d.getValue()!=null && d.getValue().length()>100))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"明细字段无效");
        }
        return service.updateDetails(details);
    }
}
