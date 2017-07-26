package com.bookstore.bookstoreapp.repository;

import com.bookstore.bookstoreapp.domain.Order;
import com.bookstore.bookstoreapp.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
	List<Order> findByUser(User user);
}
