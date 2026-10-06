package com.ruco.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {

	@Query(value = "select * from m_rate where fbo_branch_id = ?1 and is_delete = 0", nativeQuery = true)
	Optional<Rate> getRate(Long fboBranchId);

	Page<Rate> findAllByIsDeleteFalse(Pageable pageable);

}
