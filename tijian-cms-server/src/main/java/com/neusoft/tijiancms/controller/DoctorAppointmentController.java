package com.neusoft.tijiancms.controller;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/doctorAppointments")
public class DoctorAppointmentController {
 private final JdbcTemplate db;
 public DoctorAppointmentController(JdbcTemplate db){this.db=db;}
 @PostMapping("/list") public List<Map<String,Object>> list(){return db.queryForList("SELECT a.appointmentId,a.profileId,a.appointmentDate,a.queueNo,a.state,u.realName FROM doctor_appointment a JOIN users u ON a.userId=u.userId WHERE a.state='BOOKED' ORDER BY a.appointmentDate,a.profileId,a.queueNo");}
}
