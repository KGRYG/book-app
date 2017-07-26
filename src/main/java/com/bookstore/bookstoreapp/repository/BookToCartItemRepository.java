package com.bookstore.bookstoreapp.repository;


import com.bookstore.bookstoreapp.domain.BookToCartItem;
import com.bookstore.bookstoreapp.domain.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long> {
	void deleteByCartItem(CartItem cartItem);
}
