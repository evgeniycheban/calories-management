package com.caloriesmanagement.authservice.service;

import com.caloriesmanagement.authservice.model.User;
import com.caloriesmanagement.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation of {@link UserService}
 *
 * @author Evgeniy Cheban
 */
@Service
public class UserServiceImpl implements UserService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final UserRepository repository;


    /**
     * @param repository - user repository.
     */
    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(User user) {
        User existing = repository.findByUsername(user.getUsername()).orElse(null);
        Assert.isNull(existing, "User already exists: " + user.getUsername());

        String hash = ENCODER.encode(user.getPassword());
        user.setPassword(hash);

        repository.save(user);
    }

}
