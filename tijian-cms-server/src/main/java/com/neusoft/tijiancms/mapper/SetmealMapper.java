package com.neusoft.tijiancms.mapper;
import java.util.List;
import com.neusoft.tijiancms.po.Setmeal;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface SetmealMapper { List<Setmeal> list(); }
