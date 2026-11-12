package com.neusoft.tijian.controller;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/payments")
public class PaymentController {
 private final JdbcTemplate db;
 public PaymentController(JdbcTemplate db){this.db=db;}
 @Transactional @PostMapping("/payAppointment") public Map<String,Object> pay(@RequestBody Map<String,Object> input,HttpServletRequest req){
  int id;try{id=Integer.parseInt(String.valueOf(input.get("orderId")));if(id<=0)throw new NumberFormatException();}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"预约编号无效");}
  String user=(String)req.getSession(false).getAttribute("userId");
  List<Map<String,Object>> rows=db.queryForList("SELECT state,paymentStatus,paymentAmount FROM orders WHERE orderId=? AND userId=? FOR UPDATE",id,user);
  if(rows.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"预约不存在");
  Map<String,Object> row=rows.get(0);if(((Number)row.get("state")).intValue()!=1)throw new ResponseStatusException(HttpStatus.CONFLICT,"预约状态不允许支付");
  if("PAID".equals(row.get("paymentStatus")))return Collections.singletonMap("status","PAID");
  if(!"UNPAID".equals(row.get("paymentStatus")))throw new ResponseStatusException(HttpStatus.CONFLICT,"订单状态不允许支付");
  db.update("UPDATE orders SET paymentStatus='PAID',paidAt=NOW() WHERE orderId=? AND userId=?",id,user);return Collections.singletonMap("status","PAID");
 }
}
