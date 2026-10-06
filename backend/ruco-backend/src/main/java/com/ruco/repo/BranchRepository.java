package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ruco.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	Page<Branch> findByIsDeleteFalse(Pageable pageable);

	List<Branch> findAllByIsDeleteFalse();

	List<Branch> findAllByIsDeleteFalseAndStateId(Long stateId);

	List<Branch> findAllByIdAndIsDeleteFalse(Long branchId);

	List<Branch> findAllByIdInAndIsDeleteFalse(List<Long> branchIds);

	List<Branch> findAllByStateIdAndIsDeleteFalse(Long stateId);

	Page<Branch> findAll(Specification<Branch> branchHasSearchText, Pageable pageable);

	@Query(value = "select count(*) from m_branch where id = ?1 and is_delete = 0", nativeQuery = true)
	Optional<Integer> getBranchCount(Long branchId);

}
