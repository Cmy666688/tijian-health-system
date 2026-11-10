package com.neusoft.tijian.service;
import com.neusoft.tijian.mapper.HospitalMapper;
import com.neusoft.tijian.po.Hospital;
import java.util.List;
import org.springframework.stereotype.Service;
@Service
public class HospitalService {
    private final HospitalMapper mapper;
    public HospitalService(HospitalMapper mapper) { this.mapper = mapper; }
    public List<Hospital> listByState(Integer state) { return mapper.listByState(state); }
    public Hospital findById(Integer hpId) { return mapper.findById(hpId); }
}
