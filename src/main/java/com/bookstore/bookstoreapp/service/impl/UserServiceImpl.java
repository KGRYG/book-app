package com.bookstore.bookstoreapp.service.impl;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.security.Role;
import com.bookstore.bookstoreapp.repository.UserRepository;
import com.bookstore.bookstoreapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by karen on 7/10/17.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user, Role userRole) {
        User localUser = userRepository.findByEmail(user.getEmail());

        if (localUser != null) {
            LOG.info("User with username {} and email {} already exist. Nothing will be done. ", user.getUsername(), user.getEmail());
        } else {

            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (Role role : Role.values()) {
                if (role == userRole) {
                    user.setRole(role);
                    break;
                } else {
                    user.setRole(Role.ROLE_USER);
                }
            }

            localUser = userRepository.save(user);

        }

        return localUser;
    }
}
