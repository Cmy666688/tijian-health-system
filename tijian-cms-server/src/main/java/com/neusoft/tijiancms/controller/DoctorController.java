package com.neusoft.tijiancms.controller;
import com.neusoft.tijiancms.po.Doctor;
import com.neusoft.tijiancms.service.DoctorService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/doctor")
public class DoctorController {
    private final DoctorService service;
    public DoctorController(DoctorService service) { this.service=service; }
    @GetMapping("/me")
    public Doctor me(HttpServletRequest request) {
        HttpSession session=request.getSession(false);
        Doctor doctor=service.profile((String)session.getAttribute("doctorCode"));
        if(doctor==null) { session.invalidate(); throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"登录已失效"); }
        return doctor;
    }
    @PostMapping("/getDoctorByCodeByPass")
    public Doctor login(@RequestBody Doctor request, HttpServletRequest servletRequest) {
        if (request.getDocCode()==null || request.getDocCode().trim().isEmpty() || request.getDocCode().length()>20 ||
            request.getPassword()==null || request.getPassword().isEmpty() || request.getPassword().length()>20 || request.getPassword().getBytes(StandardCharsets.UTF_8).length>72)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "医生编号和密码不能为空，且长度不超过20");
        HttpSession old=servletRequest.getSession(false); if(old!=null) old.invalidate();
        Doctor authenticated=service.login(request.getDocCode(),request.getPassword());
        if(authenticated!=null) servletRequest.getSession(true).setAttribute("doctorCode",authenticated.getDocCode());
        return authenticated;
    }
}
