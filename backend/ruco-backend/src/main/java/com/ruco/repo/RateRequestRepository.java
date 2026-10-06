package com.ruco.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.RateRequest;

public interface RateRequestRepository extends JpaRepository<RateRequest, Long> {

	Page<RateRequest> findAllByIsDeleteFalse(Pageable pageable);

}
