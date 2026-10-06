package com.ruco.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Page<Company> findByIsDeleteFalse(Pageable pageable);

	List<Company> findAllByIsDeleteFalse();

}
