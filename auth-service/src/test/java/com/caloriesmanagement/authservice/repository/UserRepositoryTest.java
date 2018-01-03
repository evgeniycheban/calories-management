package com.caloriesmanagement.authservice.repository;

import com.caloriesmanagement.authservice.model.Role;
import com.caloriesmanagement.authservice.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.EnumSet;

import static org.junit.Assert.*;

/**
 * @author Evgeniy Cheban
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        User user = new User(null, "test", "test", true, EnumSet.of(Role.ROLE_USER));
        repository.save(user);
    }

    @Test
    public void shouldFindUserByUsername() {
        String username = repository.findByUsername("test")
                .map(User::getUsername)
                .orElse(null);

        assertEquals("test", username);
    }
}