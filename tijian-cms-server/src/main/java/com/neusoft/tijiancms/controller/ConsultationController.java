package com.neusoft.tijiancms.controller;

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
 
 public ConsultationController(JdbcTemplate db ) { this.db=db;  }
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
  return db.queryForList("SELECT c.consultationId,c.orderId,c.status,c.createdAt,c.updatedAt,u.realName FROM consultation c JOIN users u ON u.userId=c.userId ORDER BY c.updatedAt DESC,c.consultationId DESC");
 }
 @PostMapping("/messages") public Map<String,Object> messages(@RequestBody Map<String,Object> data,HttpServletRequest req) {
  int n=id(data); Map<String,Object> row=thread(n,null,false);
  Map<String,Object> result=new HashMap<>();result.put("consultation",row);
  result.put("messages",db.queryForList("SELECT messageId,senderType,content,createdAt FROM consultation_message WHERE consultationId=? ORDER BY messageId",n));return result;
 }
 @Transactional @PostMapping("/send") public Map<String,Object> send(@RequestBody Map<String,Object> data,HttpServletRequest req) {
  int n=id(data);String text=content(data); thread(n,null,true);
  db.update("INSERT INTO consultation_message (consultationId,senderType,senderId,content) VALUES (?,?,?,?)",n,"DOCTOR",req.getSession(false).getAttribute("doctorCode"),text);
  db.update("UPDATE consultation SET status='ANSWERED',updatedAt=NOW() WHERE consultationId=?",n); return Collections.singletonMap("ok",true);
 }
 
}
