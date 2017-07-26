package com.bookstore.bookstoreapp.service;

import com.bookstore.bookstoreapp.domain.Book;
import com.bookstore.bookstoreapp.domain.CartItem;
import com.bookstore.bookstoreapp.domain.ShoppingCart;
import com.bookstore.bookstoreapp.domain.User;

import java.util.List;

public interface CartItemService {
	
	CartItem addBookToCartItem(Book book, User user, int qty);
	
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	void removeCartItem(CartItem cartItem);
	
	CartItem findById(Long id);
	
	CartItem save(CartItem cartItem);

}
