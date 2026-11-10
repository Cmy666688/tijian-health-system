package com.neusoft.tijiancms;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/session") public Map<String,Object> session(HttpServletRequest request) {
        return Collections.singletonMap("doctorCode",request.getSession(false).getAttribute("doctorCode"));
    }
    @PostMapping("/logout") public void logout(HttpServletRequest request) {
        HttpSession session=request.getSession(false); if(session!=null) session.invalidate();
    }
}
