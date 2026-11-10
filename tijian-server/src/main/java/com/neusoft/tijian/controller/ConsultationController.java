package com.neusoft.tijian.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/consultations")
public class ConsultationController {
 private final JdbcTemplate db;
 private final com.neusoft.tijian.OrderAccess access;
 public ConsultationController(JdbcTemplate db , com.neusoft.tijian.OrderAccess access) { this.db=db; this.access=access; }
 private String content(Map<String,Object> input) {
  Object raw=input.get("content"); String s=raw instanceof String?((String)raw).trim():"";
  if(s.isEmpty()||s.length()>1000) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"问题内容须为 1 至 1000 字"); return s;
 }
 private int id(Map<String,Object> input) {
  try { int n=Integer.parseInt(String.valueOf(input.get("consultationId"))); if(n>0)return n; } catch(Exception ignored) {} throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"咨询编号无效");
 }
 private Map<String,Object> thread(int id, String uid, boolean lock) {
  List<Map<String,Object>> rows=uid==null?db.queryForList("SELECT * FROM consultation WHERE consultationId=?"+(lock?" FOR UPDATE":""),id):db.queryForList("SELECT * FROM consultation WHERE consultationId=? AND userId=?"+(lock?" FOR UPDATE":""),id,uid);
  if(rows.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"咨询不存在"); return rows.get(0);
 }
 @PostMapping("/list") public List<Map<String,Object>> list(HttpServletRequest req) {
  return db.queryForList("SELECT consultationId,orderId,status,createdAt,updatedAt FROM consultation WHERE userId=? ORDER BY updatedAt DESC,consultationId DESC",req.getSession(false).getAttribute("userId"));
 }
 @PostMapping("/messages") public Map<String,Object> messages(@RequestBody Map<String,Object> data,HttpServletRequest req) {
  int n=id(data); Map<String,Object> row=thread(n,(String)req.getSession(false).getAttribute("userId"),false);
  Map<String,Object> result=new HashMap<>();result.put("consultation",row);
  result.put("messages",db.queryForList("SELECT messageId,senderType,content,createdAt FROM consultation_message WHERE consultationId=? ORDER BY messageId",n));return result;
 }
 @Transactional @PostMapping("/send") public Map<String,Object> send(@RequestBody Map<String,Object> data,HttpServletRequest req) {
  int n=id(data);String text=content(data); thread(n,(String)req.getSession(false).getAttribute("userId"),true);
  db.update("INSERT INTO consultation_message (consultationId,senderType,senderId,content) VALUES (?,?,?,?)",n,"USER",req.getSession(false).getAttribute("userId"),text);
  db.update("UPDATE consultation SET status='WAITING',updatedAt=NOW() WHERE consultationId=?",n); return Collections.singletonMap("ok",true);
 }
 @Transactional @PostMapping("/create") public Map<String,Object> create(@RequestBody Map<String,Object> data,HttpServletRequest req) {
 String text=content(data), user=(String)req.getSession(false).getAttribute("userId"); Integer order=null;
 if(data.get("orderId")!=null){try{order=Integer.valueOf(String.valueOf(data.get("orderId")));}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"报告编号无效");} if(order<=0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"报告编号无效"); access.requireOrder(req,order,true);}
 final Integer report=order; KeyHolder key=new GeneratedKeyHolder();
 db.update(connection->{java.sql.PreparedStatement ps=connection.prepareStatement("INSERT INTO consultation (userId,orderId) VALUES (?,?)",java.sql.Statement.RETURN_GENERATED_KEYS);ps.setString(1,user);if(report==null)ps.setNull(2,java.sql.Types.INTEGER);else ps.setInt(2,report);return ps;},key);
 int n=key.getKey().intValue();db.update("INSERT INTO consultation_message (consultationId,senderType,senderId,content) VALUES (?,'USER',?,?)",n,user,text);return Collections.singletonMap("consultationId",n);
 }
}
