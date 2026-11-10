package com.neusoft.tijian.dto;
import java.time.LocalDate;
public class CalendarResponseDto {
    private LocalDate date;
    private Integer dayOfWeek;
    private boolean available;
    private Integer capacity;
    private Integer remaining;
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate value) { this.date = value; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer value) { this.dayOfWeek = value; }
    public boolean getAvailable() { return available; }
    public void setAvailable(boolean value) { this.available = value; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer value) { this.capacity=value; }
    public Integer getRemaining() { return remaining; }
    public void setRemaining(Integer value) { this.remaining=value; }
}
