package com.neusoft.tijian.service;
import com.neusoft.tijian.mapper.SetmealMapper;
import com.neusoft.tijian.po.Setmeal;
import com.neusoft.tijian.po.CheckItem;
import java.util.List;
import org.springframework.stereotype.Service;
@Service
public class SetmealService {
    private final SetmealMapper mapper;
    public SetmealService(SetmealMapper mapper) { this.mapper = mapper; }
    public List<Setmeal> listByType(Integer type) { return mapper.listByType(type); }
    public List<CheckItem> listCheckItems(Integer smId) { return mapper.listCheckItems(smId); }
    public Setmeal findById(Integer smId) { return mapper.findById(smId); }
}
