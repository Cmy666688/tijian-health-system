package com.neusoft.tijian.mapper;
import com.neusoft.tijian.po.Orders;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OrdersMapper {
    String lockUser(@Param("userId") String userId);
    Integer lockHospital(@Param("hpId") Integer hpId);
    int countOpen(@Param("userId") String userId);
    int insert(Orders order);
    List<Orders> list(@Param("userId") String userId, @Param("state") Integer state);
    Orders findById(@Param("orderId") Integer orderId);
    int deleteOpen(@Param("orderId") Integer orderId, @Param("userId") String userId);
}
