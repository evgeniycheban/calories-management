package com.caloriesmanagement.authservice.service.security;

import com.caloriesmanagement.authservice.model.User;
import com.caloriesmanagement.authservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Evgeniy Cheban
 */
public class UserDetailsServiceTest {

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Mock
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldLoadUserByUsernameWhenUserExists() {
        when(repository.findByUsername(any())).thenReturn(Optional.of(new User()));
        service.loadUserByUsername("test");
        verify(repository, times(1)).findByUsername("test");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldFailToLoadUserByUsernameWhenUserNotExists() {
        service.loadUserByUsername("test");
    }
}
