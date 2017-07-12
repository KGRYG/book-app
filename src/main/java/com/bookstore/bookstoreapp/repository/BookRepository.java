package com.bookstore.bookstoreapp.repository;

import com.bookstore.bookstoreapp.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
	List<Book> findByTitleContaining(String keyword);
}
