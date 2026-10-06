package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.Transporter;

public interface TransporterRepository extends JpaRepository<Transporter, Long> {

	List<Transporter> findAllByIsDeleteFalse();

	List<Transporter> findAllByBranchIdInAndIsDeleteFalse(List<Long> branchIds);

	List<Transporter> findAllByBranchIdAndIsDeleteFalse(Long branchId);

	Page<Transporter> findAll(Specification<Transporter> transporterHasSearchText, Pageable pageable);

	@Query(value = "select count(*) from m_transporter where branch_id = ?1 and is_delete = 0", nativeQuery = true)
	Optional<Integer> getTransporterCount(Long branchId);

}
