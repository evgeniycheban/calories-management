package com.caloriesmanagement.authservice.service;

import com.caloriesmanagement.authservice.model.User;

/**
 * User service.
 *
 * @author Evgeniy Cheban
 */
public interface UserService {
    /**
     * Creates new user.
     *
     * @param user - user data.
     * @return created user.
     */
    void save(User user);
}
