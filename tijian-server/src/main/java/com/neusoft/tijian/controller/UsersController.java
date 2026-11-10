package com.neusoft.tijian.controller;
import com.neusoft.tijian.po.Users;
import com.neusoft.tijian.service.UsersService;
import org.springframework.dao.DuplicateKeyException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService service;
    public UsersController(UsersService service) { this.service = service; }
    private void checkId(Users u) {
        if (u.getUserId() == null || !u.getUserId().matches("[0-9]{11}"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请输入11位手机号码");
    }
    @GetMapping("/me")
    public Users me(HttpServletRequest request) {
        HttpSession session=request.getSession(false);
        Users user=service.profile((String)session.getAttribute("userId"));
        if(user==null) { session.invalidate(); throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"登录已失效"); }
        return user;
    }
    @PostMapping("/getUsersById")
    public Users getUsersById(@RequestBody Users user) { checkId(user); return service.find(user.getUserId()); }
    @PostMapping("/getUsersByUserIdByPass")
    public Users login(@RequestBody Users user, HttpServletRequest request) {
        checkId(user);
        if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length()>20 || user.getPassword().getBytes(StandardCharsets.UTF_8).length>72) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密码不能为空");
        HttpSession old=request.getSession(false); if(old!=null) old.invalidate();
        Users authenticated=service.login(user.getUserId(), user.getPassword());
        if(authenticated!=null) request.getSession(true).setAttribute("userId",authenticated.getUserId());
        return authenticated;
    }
    @PostMapping("/saveUsers")
    public int saveUsers(@RequestBody Users user) {
        checkId(user);
        if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() > 20 || user.getPassword().getBytes(StandardCharsets.UTF_8).length > 72 ||
            user.getRealName() == null || user.getRealName().trim().isEmpty() || user.getRealName().length() > 20 ||
            user.getSex() == null || (user.getSex() != 0 && user.getSex() != 1) ||
            user.getIdentityCard() == null || user.getIdentityCard().length() != 18 || user.getBirthday() == null ||
            user.getUserType() == null || user.getUserType() < 1 || user.getUserType() > 3)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "注册信息不完整或格式不正确");
        try { return service.register(user); }
        catch (DuplicateKeyException e) { throw new ResponseStatusException(HttpStatus.CONFLICT, "该手机号已注册"); }
    }
}
