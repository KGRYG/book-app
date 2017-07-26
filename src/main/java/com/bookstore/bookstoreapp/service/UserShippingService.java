package com.bookstore.bookstoreapp.service;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.UserShipping;

public interface UserShippingService {
	
	UserShipping findById(Long id, User user);
	
	void removeById(Long id, User user);

}
