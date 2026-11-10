package com.neusoft.tijian.service;
import com.neusoft.tijian.mapper.UsersMapper;
import com.neusoft.tijian.po.Users;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UsersService {
    private final UsersMapper mapper;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    public UsersService(UsersMapper mapper) { this.mapper=mapper; }
    public Users find(String userId) { return mapper.findById(userId); }
    public Users profile(String userId) { return mapper.findProfile(userId); }
    public Users login(String userId, String password) {
        Users user=mapper.findCredentials(userId);
        if(user==null || user.getPassword()==null) return null;
        String stored=user.getPassword();
        boolean legacy=!stored.startsWith("$2");
        boolean valid=legacy ? MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8),password.getBytes(StandardCharsets.UTF_8)) : encoder.matches(password,stored);
        if(!valid) return null;
        if(legacy && mapper.upgradePassword(userId,stored,encoder.encode(password))==0) {
            Users current=mapper.findCredentials(userId);
            if(current==null || current.getPassword()==null || !current.getPassword().startsWith("$2") || !encoder.matches(password,current.getPassword())) return null;
        }
        user.setPassword(null); return user;
    }
    public int register(Users user) { user.setPassword(encoder.encode(user.getPassword())); user.setUserType(1); return mapper.insert(user); }
}
