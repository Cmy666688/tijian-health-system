package com.neusoft.tijiancms;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
public class SessionInterceptor implements HandlerInterceptor {
    private final String key;
    public SessionInterceptor(String key) { this.key=key; }
    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        HttpSession session=request.getSession(false);
        if (session==null || session.getAttribute(key)==null) { response.sendError(401); return false; }
        return true;
    }
}
