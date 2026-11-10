package com.neusoft.tijiancms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.tijiancms.controller.DoctorController;
import com.neusoft.tijiancms.po.Doctor;
import com.neusoft.tijiancms.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrentDoctorProfileTest {
    @Test void returnsOnlyCurrentDoctorAndDoesNotSerializePassword() throws Exception {
        DoctorService service=mock(DoctorService.class); Doctor doctor=new Doctor();
        doctor.setDocCode("doctor01"); doctor.setRealName("刘明"); doctor.setPassword("secret");
        when(service.profile("doctor01")).thenReturn(doctor);
        MockHttpServletRequest request=new MockHttpServletRequest(); request.getSession().setAttribute("doctorCode","doctor01");
        Doctor result=new DoctorController(service).me(request);
        assertEquals("刘明",result.getRealName());
        assertFalse(new ObjectMapper().writeValueAsString(result).contains("secret"));
        verify(service).profile("doctor01");
    }
    @Test void removedDoctorInvalidatesSession() {
        DoctorService service=mock(DoctorService.class); MockHttpServletRequest request=new MockHttpServletRequest();
        request.getSession().setAttribute("doctorCode","doctor01");
        ResponseStatusException e=assertThrows(ResponseStatusException.class,()->new DoctorController(service).me(request));
        assertEquals(HttpStatus.UNAUTHORIZED,e.getStatus());
    }
}
