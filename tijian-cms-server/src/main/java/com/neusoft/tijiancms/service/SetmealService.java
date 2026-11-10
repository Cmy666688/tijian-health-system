package com.neusoft.tijiancms.service;
import java.util.List;
import com.neusoft.tijiancms.po.Setmeal;
import com.neusoft.tijiancms.mapper.SetmealMapper;
import org.springframework.stereotype.Service;
@Service
public class SetmealService {
    private final SetmealMapper mapper;
    public SetmealService(SetmealMapper mapper) { this.mapper=mapper; }
    public List<Setmeal> list() { return mapper.list(); }
}
