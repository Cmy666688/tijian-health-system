package com.neusoft.tijian.mapper;
import com.neusoft.tijian.po.Setmeal;
import com.neusoft.tijian.po.CheckItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SetmealMapper {
    List<Setmeal> listByType(@Param("type") Integer type);
    List<CheckItem> listCheckItems(@Param("smId") Integer smId);
    Setmeal findById(@Param("smId") Integer smId);
}
