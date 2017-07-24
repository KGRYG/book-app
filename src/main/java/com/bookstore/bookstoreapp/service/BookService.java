package com.bookstore.bookstoreapp.service;

import com.bookstore.bookstoreapp.domain.Book;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface BookService {
	
	List<Book> findAll();
	
	Book findOne(Long id);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Book save(Book book);
	
	List<Book> blurrySearch(String title);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void removeOne(Long id);
}
