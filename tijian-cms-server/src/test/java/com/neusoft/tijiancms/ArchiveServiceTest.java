package com.neusoft.tijiancms;

import com.neusoft.tijiancms.mapper.OrdersMapper;
import com.neusoft.tijiancms.mapper.ReportMapper;
import com.neusoft.tijiancms.service.OrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArchiveServiceTest {
    @Test void refusesToArchiveUntilAllDetailsAndConclusionArePresent() {
        OrdersMapper orders=mock(OrdersMapper.class); ReportMapper reports=mock(ReportMapper.class);
        when(reports.lockOrderState(1)).thenReturn(1); when(reports.countReports(1)).thenReturn(2);
        when(reports.countDetails(1)).thenReturn(2); when(reports.countMissingDetails(1)).thenReturn(1);
        OrdersService service=new OrdersService(orders,reports);
        ResponseStatusException e=assertThrows(ResponseStatusException.class,()->service.updateState(1,2));
        assertEquals(HttpStatus.CONFLICT,e.getStatus()); verify(orders,never()).updateState(1,2);
    }
    @Test void archivesCompleteReport() {
        OrdersMapper orders=mock(OrdersMapper.class); ReportMapper reports=mock(ReportMapper.class);
        when(reports.lockOrderState(1)).thenReturn(1); when(reports.countReports(1)).thenReturn(2);
        when(reports.countDetails(1)).thenReturn(2); when(reports.countMissingDetails(1)).thenReturn(0);
        when(reports.countOverall(1)).thenReturn(1); when(orders.updateState(1,2)).thenReturn(1);
        assertEquals(1,new OrdersService(orders,reports).updateState(1,2));
    }
}
