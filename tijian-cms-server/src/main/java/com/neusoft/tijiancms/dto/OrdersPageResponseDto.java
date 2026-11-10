package com.neusoft.tijiancms.dto;
import java.util.List;
import com.neusoft.tijiancms.po.Orders;
public class OrdersPageResponseDto {
    private long total;
    private Integer pageNum;
    private Integer pageSize;
    private List<Orders> records;
    public long getTotal() { return total; }
    public void setTotal(long value) { this.total = value; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer value) { this.pageNum = value; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer value) { this.pageSize = value; }
    public List<Orders> getRecords() { return records; }
    public void setRecords(List<Orders> value) { this.records = value; }
}
