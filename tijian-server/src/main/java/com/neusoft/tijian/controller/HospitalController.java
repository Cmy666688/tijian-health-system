package com.neusoft.tijian.controller;
import com.neusoft.tijian.po.Hospital;
import com.neusoft.tijian.service.HospitalService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/hospital")
public class HospitalController {
    private final HospitalService service;
    public HospitalController(HospitalService service) { this.service = service; }
    @PostMapping("/listHospital")
    public List<Hospital> listHospital(@RequestBody Hospital request) {
        if (request.getState() == null || (request.getState() != 1 && request.getState() != 2))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "医院状态须为1或2");
        return service.listByState(request.getState());
    }
    @PostMapping("/getHospitalById")
    public Hospital getHospitalById(@RequestBody Hospital request) {
        if (request.getHpId() == null || request.getHpId() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "医院编号须为正整数");
        return service.findById(request.getHpId());
    }
}
