package com.bookstore.bookstoreapp.service;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.security.Role;
/**
 * Created by karen on 7/10/17.
 */
public interface UserService {
    User createUser(User user, Role userRole);
}
