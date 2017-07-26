package com.bookstore.bookstoreapp.repository;

import com.bookstore.bookstoreapp.domain.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

}
