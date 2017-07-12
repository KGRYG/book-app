package com.bookstore.bookstoreapp.service;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.security.UserRole;

import java.util.Set;

/**
 * Created by karen on 7/10/17.
 */
public interface UserService {
    User createUser(User user, Set<UserRole> userRoles);
}
