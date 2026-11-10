package com.neusoft.tijiancms.service;
import com.neusoft.tijiancms.mapper.DoctorMapper;
import com.neusoft.tijiancms.po.Doctor;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class DoctorService {
    private final DoctorMapper mapper;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    public DoctorService(DoctorMapper mapper) { this.mapper=mapper; }
    public Doctor profile(String docCode) { return mapper.findProfile(docCode); }
    public Doctor login(String docCode, String password) {
        Doctor doctor=mapper.findCredentials(docCode);
        if(doctor==null || doctor.getPassword()==null) return null;
        String stored=doctor.getPassword(); boolean legacy=!stored.startsWith("$2");
        boolean valid=legacy ? MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8),password.getBytes(StandardCharsets.UTF_8)) : encoder.matches(password,stored);
        if(!valid) return null;
        if(legacy && mapper.upgradePassword(docCode,stored,encoder.encode(password))==0) {
            Doctor current=mapper.findCredentials(docCode);
            if(current==null || current.getPassword()==null || !current.getPassword().startsWith("$2") || !encoder.matches(password,current.getPassword())) return null;
        }
        doctor.setPassword(null); return doctor;
    }
}
