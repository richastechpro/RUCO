package com.ruco.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruco.model.FBOBranchLogin;

public interface FboBranchLoginRepository extends JpaRepository<FBOBranchLogin, Long> {

	Optional<FBOBranchLogin> findByEmailAndIsDeleteFalse(String loginId);

}
