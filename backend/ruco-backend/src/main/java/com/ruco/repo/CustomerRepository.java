package com.ruco.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findAllByIsDeleteFalse();

	Page<Customer> findAll(Specification<Customer> customerHasSearchText, Pageable pageable);

}
