package com.bookstore.bookstoreapp.repository;

import com.bookstore.bookstoreapp.domain.UserPayment;
import org.springframework.data.repository.CrudRepository;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {

}
