package com.bookstore.bookstoreapp.repository;

import com.bookstore.bookstoreapp.domain.ShippingAddress;
import org.springframework.data.repository.CrudRepository;

public interface ShippingAddressRepository extends CrudRepository<ShippingAddress, Long> {
	
}
