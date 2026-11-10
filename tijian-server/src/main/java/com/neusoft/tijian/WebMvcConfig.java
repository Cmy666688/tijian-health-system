package com.neusoft.tijian;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor("userId")).addPathPatterns("/**").excludePathPatterns("/users/getUsersByUserIdByPass", "/users/saveUsers", "/users/getUsersById");
    }
    @Override public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8081", "http://127.0.0.1:8081")
            .allowedMethods("GET", "POST", "OPTIONS").allowedHeaders("*").allowCredentials(true);
    }
}
