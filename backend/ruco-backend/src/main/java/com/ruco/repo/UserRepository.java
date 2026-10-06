package com.ruco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByLoginIdAndIsDeleteFalse(String loginId);

	List<User> findAllByUserTypeAndIsDeleteFalse(String userType);

	List<User> findAllByUserTypeAndBranchIdAndIsDeleteFalse(String pickupboy, Long branchId);

	List<User> findAllByUserTypeAndBranchIdInAndIsDeleteFalse(String pickupboy, List<Long> branchIds);

	Page<User> findAll(Specification<User> userHasSearchText, Pageable pageable);

}
