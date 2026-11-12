package com.neusoft.tijian.controller;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/healthServices")
public class HealthServicesController {
 private final JdbcTemplate db;
 public HealthServicesController(JdbcTemplate db){this.db=db;}
 private String user(HttpServletRequest req){return (String)req.getSession(false).getAttribute("userId");}
 private String text(Map<String,Object> body,String key,int max,boolean required){Object raw=body.get(key);String s=raw instanceof String?((String)raw).trim():"";if((required&&s.isEmpty())||s.length()>max)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"内容无效");return s;}
 private int id(Map<String,Object> body,String key){try{int n=Integer.parseInt(String.valueOf(body.get(key)));if(n>0)return n;}catch(Exception ignored){}throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"编号无效");}
 @PostMapping("/request") public Map<String,Object> create(@RequestBody Map<String,Object> b,HttpServletRequest req){String type=text(b,"serviceType",24,true);if(!Arrays.asList("HOME_CARE","GROUP_EXAM").contains(type))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"服务类型无效");db.update("INSERT INTO service_request(userId,serviceType,content) VALUES(?,?,?)",user(req),type,text(b,"content",1000,true));return Collections.singletonMap("ok",true);}
 @PostMapping("/requests") public List<Map<String,Object>> requests(HttpServletRequest req){return db.queryForList("SELECT requestId,serviceType,content,status,reply,createdAt,updatedAt FROM service_request WHERE userId=? ORDER BY requestId DESC",user(req));}
 @PostMapping("/record") public Map<String,Object> record(@RequestBody Map<String,Object> b,HttpServletRequest req){String category=text(b,"category",20,true);if(!Arrays.asList("血压","血糖","体重","其他").contains(category))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"记录类型无效");LocalDate date;try{date=LocalDate.parse(text(b,"recordDate",10,true));}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"日期无效");}if(date.isAfter(LocalDate.now()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"日期不能晚于今天");db.update("INSERT INTO health_record(userId,recordDate,category,value,note) VALUES(?,?,?,?,?)",user(req),date,category,text(b,"value",80,true),text(b,"note",500,false));return Collections.singletonMap("ok",true);}
 @PostMapping("/records") public List<Map<String,Object>> records(HttpServletRequest req){return db.queryForList("SELECT recordId,recordDate,category,value,note FROM health_record WHERE userId=? ORDER BY recordDate DESC,recordId DESC LIMIT 200",user(req));}
 @PostMapping("/deleteRecord") public Map<String,Object> delete(@RequestBody Map<String,Object> b,HttpServletRequest req){if(db.update("DELETE FROM health_record WHERE recordId=? AND userId=?",id(b,"recordId"),user(req))==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"记录不存在");return Collections.singletonMap("ok",true);}
 @PostMapping("/products") public List<Map<String,Object>> products(){return db.queryForList("SELECT productId,title,description,price FROM shop_product WHERE active=1 ORDER BY productId");}
 @PostMapping("/buy") public Map<String,Object> buy(@RequestBody Map<String,Object> b,HttpServletRequest req){int n=db.update("INSERT INTO shop_order(userId,productId,title,amount) SELECT ?,productId,title,price FROM shop_product WHERE productId=? AND active=1",user(req),id(b,"productId"));if(n==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"商品不存在");return Collections.singletonMap("ok",true);}
 @PostMapping("/shopOrders") public List<Map<String,Object>> orders(HttpServletRequest req){return db.queryForList("SELECT o.shopOrderId,o.title,o.amount,o.state,o.createdAt,CASE WHEN o.state='PAID' THEN p.digitalContent ELSE NULL END AS digitalContent FROM shop_order o JOIN shop_product p ON p.productId=o.productId WHERE o.userId=? ORDER BY o.shopOrderId DESC",user(req));}
 @Transactional @PostMapping("/payShop") public Map<String,Object> pay(@RequestBody Map<String,Object> b,HttpServletRequest req){int n=id(b,"shopOrderId");List<Map<String,Object>> rows=db.queryForList("SELECT state FROM shop_order WHERE shopOrderId=? AND userId=? FOR UPDATE",n,user(req));if(rows.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"订单不存在");String state=(String)rows.get(0).get("state");if("CANCELLED".equals(state))throw new ResponseStatusException(HttpStatus.CONFLICT,"订单已取消");if("UNPAID".equals(state))db.update("UPDATE shop_order SET state='PAID',paidAt=NOW() WHERE shopOrderId=?",n);return Collections.singletonMap("ok",true);}
 @PostMapping("/cancelShop") public Map<String,Object> cancel(@RequestBody Map<String,Object> b,HttpServletRequest req){if(db.update("UPDATE shop_order SET state='CANCELLED' WHERE shopOrderId=? AND userId=? AND state='UNPAID'",id(b,"shopOrderId"),user(req))==0)throw new ResponseStatusException(HttpStatus.CONFLICT,"订单不能取消");return Collections.singletonMap("ok",true);}
}
