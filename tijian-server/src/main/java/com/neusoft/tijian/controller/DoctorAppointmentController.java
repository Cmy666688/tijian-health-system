package com.neusoft.tijian.controller;
import java.time.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/doctorAppointments")
public class DoctorAppointmentController {
 private final JdbcTemplate db;
 public DoctorAppointmentController(JdbcTemplate db){this.db=db;}
 private String user(HttpServletRequest r){return (String)r.getSession(false).getAttribute("userId");}
 private String profile(Map<String,Object> data){Object v=data.get("profileId");if(!(v instanceof String)||!((String)v).matches("doctor-0[1-8]"))throw bad("医生编号无效");return (String)v;}
 private ResponseStatusException bad(String message){return new ResponseStatusException(HttpStatus.BAD_REQUEST,message);}
 private LocalDate date(Map<String,Object> data){try{return LocalDate.parse(String.valueOf(data.get("date")));}catch(Exception e){throw bad("日期无效");}}
 private int id(Map<String,Object> data){try{int n=Integer.parseInt(String.valueOf(data.get("appointmentId")));if(n>0)return n;}catch(Exception ignored){}throw bad("预约编号无效");}
 private Map<String,Object> schedule(String profile,boolean lock){List<Map<String,Object>> rows=db.queryForList("SELECT profileId,dailyCapacity,active FROM doctor_schedule WHERE profileId=?"+(lock?" FOR UPDATE":""),profile);if(rows.isEmpty()||((Number)rows.get(0).get("active")).intValue()!=1)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"排班不存在");return rows.get(0);}
 @PostMapping("/calendar") public List<Map<String,Object>> calendar(@RequestBody Map<String,Object> body){String p=profile(body);int capacity=((Number)schedule(p,false).get("dailyCapacity")).intValue();LocalDate today=LocalDate.now(ZoneId.of("Asia/Shanghai"));List<Map<String,Object>> result=new ArrayList<>();for(int i=1;i<=30;i++){LocalDate day=today.plusDays(i);if(day.getDayOfWeek().getValue()>5)continue;Integer count=db.queryForObject("SELECT COUNT(*) FROM doctor_appointment WHERE profileId=? AND appointmentDate=? AND state='BOOKED'",Integer.class,p,day);Map<String,Object> row=new LinkedHashMap<>();row.put("date",day.toString());row.put("capacity",capacity);row.put("remaining",Math.max(0,capacity-count));result.add(row);}return result;}
 @Transactional @PostMapping("/book") public Map<String,Object> book(@RequestBody Map<String,Object> body,HttpServletRequest req){String p=profile(body),u=user(req);LocalDate day=date(body),today=LocalDate.now(ZoneId.of("Asia/Shanghai"));if(!day.isAfter(today)||day.isAfter(today.plusDays(30))||day.getDayOfWeek().getValue()>5)throw bad("请选择未来 30 天内开放的工作日");int capacity=((Number)schedule(p,true).get("dailyCapacity")).intValue();Integer existing=db.queryForObject("SELECT COUNT(*) FROM doctor_appointment WHERE profileId=? AND appointmentDate=? AND userId=? AND state='BOOKED'",Integer.class,p,day,u);if(existing>0)throw new ResponseStatusException(HttpStatus.CONFLICT,"当天已有该医生预约");Integer count=db.queryForObject("SELECT COUNT(*) FROM doctor_appointment WHERE profileId=? AND appointmentDate=? AND state='BOOKED'",Integer.class,p,day);if(count>=capacity)throw new ResponseStatusException(HttpStatus.CONFLICT,"当天名额已满");Integer number=db.queryForObject("SELECT COALESCE(MAX(queueNo),0)+1 FROM doctor_appointment WHERE profileId=? AND appointmentDate=?",Integer.class,p,day);KeyHolder key=new GeneratedKeyHolder();db.update(c->{java.sql.PreparedStatement s=c.prepareStatement("INSERT INTO doctor_appointment(profileId,userId,appointmentDate,queueNo) VALUES (?,?,?,?)",java.sql.Statement.RETURN_GENERATED_KEYS);s.setString(1,p);s.setString(2,u);s.setDate(3,java.sql.Date.valueOf(day));s.setInt(4,number);return s;},key);Map<String,Object> result=new HashMap<>();result.put("appointmentId",key.getKey().intValue());result.put("queueNo",number);return result;}
 @PostMapping("/list") public List<Map<String,Object>> list(HttpServletRequest req){return db.queryForList("SELECT appointmentId,profileId,appointmentDate,queueNo,state,createdAt FROM doctor_appointment WHERE userId=? ORDER BY appointmentDate DESC,appointmentId DESC",user(req));}
 @Transactional @PostMapping("/cancel") public Map<String,Object> cancel(@RequestBody Map<String,Object> body,HttpServletRequest req){int id=id(body);String u=user(req);List<Map<String,Object>> rows=db.queryForList("SELECT profileId FROM doctor_appointment WHERE appointmentId=? AND userId=?",id,u);if(rows.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"预约不存在");schedule((String)rows.get(0).get("profileId"),true);int n=db.update("UPDATE doctor_appointment SET state='CANCELLED',activeMarker=NULL WHERE appointmentId=? AND userId=? AND state='BOOKED' AND appointmentDate>=?",id,u,LocalDate.now(ZoneId.of("Asia/Shanghai")));if(n==0)throw new ResponseStatusException(HttpStatus.CONFLICT,"该预约不能取消");return Collections.singletonMap("ok",true);}
}
