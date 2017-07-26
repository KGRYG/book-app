package com.bookstore.bookstoreapp.repository;

import com.bookstore.bookstoreapp.domain.CartItem;
import com.bookstore.bookstoreapp.domain.ShoppingCart;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
}
