package com.bookstore.bookstoreapp.service.impl;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.UserPayment;
import com.bookstore.bookstoreapp.repository.UserPaymentRepository;
import com.bookstore.bookstoreapp.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {
	@Autowired
	private UserPaymentRepository userPaymentRepository;
	
	public UserPayment findById(Long id, User user) {
		return user.getUserPaymentList().stream().filter(item -> item.getId() == id).findFirst().orElse(null);
	}
	
	public void removeById(Long id, User user) {
		UserPayment userPayment = user.getUserPaymentList().stream().filter(item -> item.getId() == id).findFirst().orElse(null);
		if (userPayment != null) {
			userPaymentRepository.delete(id);
		}
	}
}
