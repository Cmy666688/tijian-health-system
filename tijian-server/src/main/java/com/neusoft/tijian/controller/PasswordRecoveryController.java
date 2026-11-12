package com.neusoft.tijian.controller;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/passwordRecovery")
public class PasswordRecoveryController {
 private final JdbcTemplate db;
 public PasswordRecoveryController(JdbcTemplate db){this.db=db;}
 @PostMapping("/request") public Map<String,Object> request(@RequestBody Map<String,Object> input){
  Object value=input.get("userId");if(!(value instanceof String)||!((String)value).matches("[0-9]{11}"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"手机号格式不正确");
  // 统一响应，且不接受用户提交的新密码或身份信息。
  db.update("INSERT INTO password_reset_request(userId) SELECT userId FROM users WHERE userId=? ON DUPLICATE KEY UPDATE requestId=requestId",value);
  return Collections.singletonMap("ok",true);
 }
}
