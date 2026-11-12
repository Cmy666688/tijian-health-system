package com.neusoft.tijiancms.controller;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/healthServices")
public class HealthServicesController {
 private final JdbcTemplate db;
 public HealthServicesController(JdbcTemplate db){this.db=db;}
 @PostMapping("/requests") public List<Map<String,Object>> list(){return db.queryForList("SELECT r.requestId,r.serviceType,r.content,r.status,r.reply,r.createdAt,u.realName FROM service_request r JOIN users u ON u.userId=r.userId ORDER BY r.requestId DESC");}
 @PostMapping("/reply") public Map<String,Object> reply(@RequestBody Map<String,Object> b){int id;try{id=Integer.parseInt(String.valueOf(b.get("requestId")));if(id<=0)throw new NumberFormatException();}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"编号无效");}Object raw=b.get("reply");String reply=raw instanceof String?((String)raw).trim():"";if(reply.isEmpty()||reply.length()>1000)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"回复内容无效");if(db.update("UPDATE service_request SET status='REPLIED',reply=?,updatedAt=NOW() WHERE requestId=?",reply,id)==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"申请不存在");return Collections.singletonMap("ok",true);}
}
