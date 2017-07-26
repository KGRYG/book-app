package com.bookstore.bookstoreapp.service.impl;

import com.bookstore.bookstoreapp.config.SecurityUtility;
import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.UserBilling;
import com.bookstore.bookstoreapp.domain.UserPayment;
import com.bookstore.bookstoreapp.domain.UserShipping;
import com.bookstore.bookstoreapp.domain.security.Role;
import com.bookstore.bookstoreapp.repository.UserBillingRepository;
import com.bookstore.bookstoreapp.repository.UserPaymentRepository;
import com.bookstore.bookstoreapp.repository.UserRepository;
import com.bookstore.bookstoreapp.repository.UserShippingRepository;
import com.bookstore.bookstoreapp.service.UserService;
import com.bookstore.bookstoreapp.utility.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private UserBillingRepository userBillingRepository;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private UserShippingRepository userShippingRepository;

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

    //todo apply user check
    @Override
    public void updateUserBilling(UserPayment userPayment, User user) {
        userPayment.setUser(user);
        userPaymentRepository.save(userPayment);
    }

    @Override
    public void setUserDefaultPayment(Long userPaymentId, User user) {
        boolean exists = user.getUserPaymentList().stream().anyMatch(item -> item.getId() == userPaymentId);
        if (exists) {
            List <UserPayment> userPayments = user.getUserPaymentList().stream().map(item -> {
                if (item.getId() == userPaymentId) {
                    item.setDefaultPayment(true);
                } else {
                    item.setDefaultPayment(false);
                }
                return item;
            }).collect(Collectors.toList());
            userPaymentRepository.save(userPayments);
        }
    }

    //todo apply user check
    @Override
    public void updateUserShipping(UserShipping userShipping, User user) {
        user.addUserShipping(userShipping);
        save(user);
    }
    //todo apply user check
    @Override
    public void setUserDefaultShipping(Long userShippingId, User user) {
        boolean exists = user.getUserShippingList().stream().anyMatch(item -> item.getId() == userShippingId);

        if (exists) {
            List <UserShipping> userShippingList = user.getUserShippingList().stream().map(item -> {
                if (item.getId() == userShippingId) {
                    item.setUserShippingDefault(true);
                } else {
                    item.setUserShippingDefault(false);
                }
                return item;
            }).collect(Collectors.toList());
            userShippingRepository.save(userShippingList);
        }
    }
}
