package com.bookstore.bookstoreapp.service;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.UserBilling;
import com.bookstore.bookstoreapp.domain.UserPayment;
import com.bookstore.bookstoreapp.domain.UserShipping;
import com.bookstore.bookstoreapp.domain.security.Role;
/**
 * Created by karen on 7/10/17.
 */
public interface UserService {
    User createUser(User user, Role userRole);

    User save(User user);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    void updateUserBilling(UserPayment userPayment, User user);

    void setUserDefaultPayment(Long userPaymentId, User user);

    void updateUserShipping(UserShipping userShipping, User user);

    void setUserDefaultShipping(Long userShippingId, User user);
}
