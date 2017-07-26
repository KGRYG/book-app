package com.bookstore.bookstoreapp.service;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.UserPayment;

public interface UserPaymentService {
	UserPayment findById(Long id, User user);
	void removeById(Long id, User user);
}
