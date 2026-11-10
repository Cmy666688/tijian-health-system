package com.neusoft.tijian.po;
import java.time.LocalDate;
public class DailyBooked {
    private LocalDate orderDate;
    private Integer bookedCount;
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate value) { this.orderDate=value; }
    public Integer getBookedCount() { return bookedCount; }
    public void setBookedCount(Integer value) { this.bookedCount=value; }
}
