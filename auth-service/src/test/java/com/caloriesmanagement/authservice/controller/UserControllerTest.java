package com.caloriesmanagement.authservice.controller;

import com.caloriesmanagement.authservice.model.Role;
import com.caloriesmanagement.authservice.model.User;
import com.caloriesmanagement.authservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.EnumSet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class UserControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @InjectMocks
    private UserController controller;

    @Mock
    private UserService service;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldSaveUser() throws Exception {
        User user = new User(null, "test", "test", true, EnumSet.of(Role.ROLE_USER));
        String json = MAPPER.writeValueAsString(user);
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailToSaveUserWhenUserIsNotValid() throws Exception {
        String json = MAPPER.writeValueAsString(new User());
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }
}
