package com.neusoft.tijian.service;
import com.neusoft.tijian.mapper.ReportMapper;
import com.neusoft.tijian.po.CiReport;
import com.neusoft.tijian.po.OverallResult;
import java.util.List;
import org.springframework.stereotype.Service;
@Service
public class ReportService {
    private final ReportMapper mapper;
    public ReportService(ReportMapper mapper) { this.mapper=mapper; }
    public List<OverallResult> listOverallResults(Integer orderId) { return mapper.listOverallResults(orderId); }
    public List<CiReport> listCiReports(Integer orderId) { return mapper.listCiReports(orderId); }
}
