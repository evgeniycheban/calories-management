package com.caloriesmanagement.authservice.service;

import com.caloriesmanagement.authservice.model.Role;
import com.caloriesmanagement.authservice.model.User;
import com.caloriesmanagement.authservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.EnumSet;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Evgeniy Cheban
 */
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldSaveUser() {
        when(repository.findByUsername("test")).thenReturn(Optional.empty());
        User user = new User(null, "test", "test", true, EnumSet.of(Role.ROLE_USER));
        service.save(user);
        verify(repository, times(1)).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUserAlreadyExists() {
        when(repository.findByUsername("test")).thenReturn(Optional.of(new User()));
        User user = new User(null, "test", "test", true, EnumSet.of(Role.ROLE_USER));
        service.save(user);
    }
}