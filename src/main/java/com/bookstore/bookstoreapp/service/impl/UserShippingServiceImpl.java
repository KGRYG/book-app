package com.bookstore.bookstoreapp.service.impl;

import com.bookstore.bookstoreapp.domain.User;
import com.bookstore.bookstoreapp.domain.UserShipping;
import com.bookstore.bookstoreapp.repository.UserShippingRepository;
import com.bookstore.bookstoreapp.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingServiceImpl implements UserShippingService {
	
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	public UserShipping findById(Long id, User user) {
		return user.getUserShippingList().stream().filter(item -> item.getId() == id).findFirst().orElse(null);
	}
	
	public void removeById(Long id, User user) {
		boolean exists = user.getUserShippingList().stream().anyMatch(item -> item.getId() == id);
		if (exists) userShippingRepository.delete(id);
	}
}
