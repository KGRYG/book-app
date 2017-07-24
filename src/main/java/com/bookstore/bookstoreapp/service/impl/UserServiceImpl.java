package com.bookstore.bookstoreapp.service.impl;

import com.bookstore.bookstoreapp.config.SecurityUtility;
import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.security.Role;
import com.bookstore.bookstoreapp.repository.UserRepository;
import com.bookstore.bookstoreapp.service.UserService;
import com.bookstore.bookstoreapp.utility.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by karen on 7/10/17.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Override
    public User createUser(User user, Role userRole) {
        User localUser = userRepository.findByEmail(user.getEmail());

        if (localUser != null) {
            LOG.info("User with username {} and email {} already exist. Nothing will be done. ", user.getUsername(), user.getEmail());
        } else {

            String randomPassword = SecurityUtility.randomPassword();
            user.setPassword(passwordEncoder.encode(randomPassword));

            for (Role role : Role.values()) {
                if (role == userRole) {
                    user.setRole(role);
                    break;
                } else {
                    user.setRole(Role.ROLE_USER);
                }
            }

            localUser = userRepository.save(user);
            mailService.sendNewUserEmail(localUser, randomPassword);

        }

        return localUser;
    }

    @Override
    public User save(User user)  {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
