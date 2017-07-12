package com.bookstore.bookstoreapp.repository;

import com.bookstore.bookstoreapp.domain.security.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
