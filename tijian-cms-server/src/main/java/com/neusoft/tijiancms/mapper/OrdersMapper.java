package com.neusoft.tijiancms.mapper;
import java.util.List;
import com.neusoft.tijiancms.dto.OrdersPageRequestDto;
import com.neusoft.tijiancms.po.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OrdersMapper {
    long count(@Param("filter") OrdersPageRequestDto filter);
    List<Orders> list(@Param("filter") OrdersPageRequestDto filter, @Param("offset") long offset, @Param("limit") int limit);
    Orders findById(@Param("orderId") Integer orderId);
    int updateState(@Param("orderId") Integer orderId, @Param("state") Integer state);
}
