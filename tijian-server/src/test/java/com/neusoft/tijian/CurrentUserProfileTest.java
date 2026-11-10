package com.neusoft.tijian;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.tijian.controller.UsersController;
import com.neusoft.tijian.po.Users;
import com.neusoft.tijian.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrentUserProfileTest {
    @Test void returnsOnlyCurrentAccountAndDoesNotSerializePassword() throws Exception {
        UsersService service=mock(UsersService.class); Users user=new Users();
        user.setUserId("19900000001"); user.setRealName("林晨"); user.setPassword("secret");
        when(service.profile("19900000001")).thenReturn(user);
        MockHttpServletRequest request=new MockHttpServletRequest(); request.getSession().setAttribute("userId","19900000001");
        Users result=new UsersController(service).me(request);
        assertEquals("林晨",result.getRealName());
        assertFalse(new ObjectMapper().writeValueAsString(result).contains("secret"));
        verify(service).profile("19900000001");
    }
    @Test void removedAccountInvalidatesSession() {
        UsersService service=mock(UsersService.class); MockHttpServletRequest request=new MockHttpServletRequest();
        request.getSession().setAttribute("userId","19900000001");
        ResponseStatusException e=assertThrows(ResponseStatusException.class,()->new UsersController(service).me(request));
        assertEquals(HttpStatus.UNAUTHORIZED,e.getStatus());
    }
}
